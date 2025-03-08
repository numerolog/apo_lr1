
        const client = new StompJs.Client({
          brokerURL: 'ws://127.0.0.1:8080/ws',
          connectHeaders: {},
          // debug: (str) => console.log(str),
          reconnectDelay: 5000,
          heartbeatIncoming: 4000,
          heartbeatOutgoing: 4000,
        });

        function toast(clz, message)
        {
            Toastify({
              text: message,
              className: clz,
            }).showToast();
        }

        var reqCounter = 0;
        function request(path, bodySrc, onOk, onError)
        {
            var body = Object.assign({}, bodySrc);
            var request_id = parseInt(Math.random()*100000000000 + (reqCounter++)) + '';
            // request_id отправляется в body для получения в body-ответа, и в headers, для обработчика в спринге(мне лень мучаться с пробросом BaseRequest в GlobalExceptionHandler)
            body['request_id'] = request_id;

            if (!client.__requests)
                client.__requests = {};

            if (!client.__registeredPaths)
                client.__registeredPaths = {};

            if (!client.__hasErrorHandler)
                client.__hasErrorHandler = 'no';

            client.__requests[request_id] = [onOk, onError];

            var fullPath = '/app' + path;
            if (!(fullPath in client.__registeredPaths))
            {
                client.subscribe('/user' + path, (frame) => 
                {
                    var bodyParsed = undefined;
                    try
                    {
                        bodyParsed = JSON.parse(frame.body);
                    } catch (e) 
                    {
                        toast('fail', 'Unknown data received at data handler for path=' + path);
                        return;
                    }

                    if (!bodyParsed.request_id)
                    {
                        toast('fail', 'Received data with unknown request id for path=' + path);
                        return;
                    }
                    client.__requests[bodyParsed.request_id][0](bodyParsed);
                });

                client.__registeredPaths[fullPath] = 'yes';
            }

            if (client.__hasErrorHandler == 'no')
            {
                client.subscribe('/user/error', (frame) =>
                {
                    var bodyParsed = undefined;
                    try
                    {
                        bodyParsed = JSON.parse(frame.body);
                    } catch (e) 
                    {
                        toast('fail', 'Unknown data received at error handler');
                        return;
                    }

                    if (!bodyParsed.request_id)
                    {
                        toast('fail', 'Received error with unknown request id');
                        return;
                    }

                    client.__requests[bodyParsed.request_id][1](bodyParsed);
                });
                client.__hasErrorHandler = 'yes';
            }

            client.publish({destination: fullPath, headers: {'request_id': request_id}, body: JSON.stringify(body)}); 
        }

        function subscribe(path, callback)
        {
            client.subscribe('/user' + path, (frame) => 
            {
                var bodyParsed = undefined;
                try
                {
                    bodyParsed = JSON.parse(frame.body);
                } catch (e) 
                {
                    toast('fail', 'Unknown data received at data handler for path=' + path);
                    return;
                }

                callback(bodyParsed);
            });
        }

        var NAME_CACHE = {};
        function requestName(user_id, callback)
        {
            if (NAME_CACHE[user_id] !== undefined)
            {
                callback(NAME_CACHE[user_id], user_id);
            }
            else
            {
                request('/uctl/name', {'id': user_id}, 
                    (response) => 
                    {
                        NAME_CACHE[user_id] = response.name;
                        callback(NAME_CACHE[user_id], user_id);
                    }, 
                    (error) =>
                    {
                        toast('fail', 'Ошибка получения имени для id=' + user_id);
                    });
            }
        }

        function renderMessage(message)
        {
            let messagesHtml = '';
            messagesHtml += `<span class="me-2 text-secondary" id="message_${message.id}_flags">` + (message.readen ? '🗹' : '[ ]') +`</span>`;
            messagesHtml += `<span class="me-2 text-secondary" id="sendername_${message.id}"></span>`;
            messagesHtml += `${message.text}`;
            $('#message_'+message.id).html(messagesHtml);

            requestName(message.author_id, (name)=>$('#sendername_'+message.id).text(name));
        }

        function addMessage(message, action)
        {
            var doPrepend = action === 'prepend'; 
            var doAppend = action === 'append'; 
            var doRepaint = action === 'repaint'; 
            var doFirst = action === 'first'; 

            let messagesHtml = '';
            messagesHtml += `<p id="message_${message.id}"></p>`;

            if (doAppend)
                $('#chatBody').append(messagesHtml);
            else if (doPrepend || doFirst)
                $('#chatBody').prepend(messagesHtml);
            
            renderMessage(message);
        }

        var LAST_HASH = '';
        var CURRENT_USER_ID = -1;
        var CONVERSATION_LIST = [];
        var CONVERSATION = {'id': -1, 'offset_from': -1, 'offset_size': 10};
        var CONVERSATION_MINIMAL_MESSAGE_ID = 999999999;
        var CONVERSATION_MEMBERS = [];
        var CONVERSATION_MEMBERS_NEW = [];

        function processEvent(type, args)
        {
			console.log('E:' + type);
            switch (type)
            {
                case 'connected':
                {
                    subscribe('/notice/newmessage', (notice) => {
                        if (notice.chat_id != CONVERSATION.id)
                        {
                            $('#conv_'+notice.chat_id+'_link').addClass('fw-bold');
                            return;
                        }
                        pushEvent('/conversation/load', {'id': CONVERSATION.id, 'offset_from': notice.message_id, 'offset_size': 1, 'action': 'append'});
                    });
                    subscribe('/notice/addremove_user', (notice) => {
                        if (CURRENT_USER_ID != notice.user_id)
                            return;
            
                        toast(notice.removed ? 'fail' : 'ok', (notice.removed ? 'Вас удалили из чата #' : 'Вас добавили в чат #') + notice.chat_id);
                        pushEvent('/conversation/list');
                    });
                    subscribe('/notice/addremove_message_flag', (notice) => {
                        if (notice.chat_id != CONVERSATION.id)
                        {
                            $('#conv_'+notice.chat_id+'_link').addClass('fw-bold');
                            return;
                        }

                        pushEvent('/conversation/load', {'id': CONVERSATION.id, 'offset_from': notice.message_id, 'offset_size': 1, 'action': 'repaint'});
                    });

                    $('#formContainer').hide();
                    $('#connectForm').hide();

                    var token = localStorage.getItem('token');
                    if (token)
                    {
                        console.log('has token'+token+', try back in');
                        request('/uctl/back', {'token': token}, 
                            (response) => 
                            {
                                pushEvent('logged', response);
                            }, 
                            (error) =>
                            {
                                pushEvent('login_by_token_failed');
                            });
                    }
                    else
                    {
                        console.log('no token, display auth form...');
                        pushEvent('login_required');
                    }
                } break;

                case 'login_by_token_failed':
                {
                    localStorage.removeItem('token');
                    toast('fail', 'Токен устарел');
                    pushEvent('login_required');
                } break;

                case 'login_required':
                {
                    $('#chat_container').fadeOut();
                    $('#formContainer').fadeIn();
                    $('#loginForm').fadeIn();
                } break;

                case '/uctl/logout':
                {
                    request('/uctl/logout', args, 
                        (response) => 
                        {
                            localStorage.removeItem('token');
                            toast('fail', 'Выход выполнен');
                            pushEvent('login_required');
                        }, 
                        (error) =>
                        {
                            pushEvent('logout_failed');
                        });
                } break;

                case 'logout_failed':
                {
                    toast('fail', 'Ошибка выхода');
                } break;

                case '/uctl/login':
                {
                    request('/uctl/login', args, 
                        (response) => 
                        {
                            pushEvent('logged', response);
                        }, 
                        (error) =>
                        {
                            pushEvent('login_by_credentials_failed');
                        });
                } break;

                case 'login_by_credentials_failed':
                {
                    toast('fail', 'Неверный логин и/или пароль');
                } break;

                case '/uctl/register':
                {
                    request('/uctl/register', args, 
                        (response) => 
                        {
                            pushEvent('logged', response);
                        }, 
                        (error) =>
                        {
                            pushEvent('register_by_credentials_failed', error.message);
                        });
                }
                break;

                case 'register_by_credentials_failed':
                {
                    toast('fail', 'Ошибка регистрации: ' + args);
                } break;

                case 'logged':
                {
                    localStorage.setItem('token', args.token);
                    toast('ok', 'Вход выполнен');
                    $('#chat_container').fadeIn();
                    $('#formContainer').fadeOut();
                    $('#loginForm').fadeOut();
                    $('#registerForm').fadeOut();

                    CURRENT_USER_ID = args.id;
                    requestName(args.id, (name) => $('#selfName').text(name));

                    pushEvent('/conversation/list');
                } break;

                case '/conversation/list':
                {
                    request('/conversation/list', args, 
                        (response) => 
                        {
                            var channelList = $('#channelList');
                            channelList.text('');
                            CONVERSATION_LIST = response.list;
                            for (var conv of CONVERSATION_LIST)
                                channelList.append('<li><p id="conv_'+conv.id+'_link"><a href="#conv_'+conv.id+'">'+conv.name+'</a></p></li>');
                            pushEvent('hashchanged')
                        }, 
                        (error) =>
                        {
                            toast('fail', 'Ошибка загрузки диалогов');
                        });
                } break;
                case '/conversation/create':
                {
                    request('/conversation/create', args, 
                        (response) => 
                        {
                            $('#formContainer').fadeOut();
                            $('#createConservationForm').fadeOut();
                            pushEvent('/conversation/list');
                        }, 
                        (error) =>
                        {
                            toast('fail', 'Ошибка создания диалога: ' + error.message);
                        });
                } break;

                case '/conversation/load':
                {
                    var conversationChanged = CONVERSATION.id != args.id;
                    CONVERSATION = args;
                    var doPrepend = args.action === 'prepend'; 
                    var doAppend = args.action === 'append'; 
                    var doRepaint = args.action === 'repaint'; 
                    var doFirst = args.action === 'first'; 

                    if (conversationChanged) // !doPrepend && !doAppend && !doRepaint)
                        CONVERSATION_MINIMAL_MESSAGE_ID = 999999999;

                    for (var conv of CONVERSATION_LIST)
                    {
                        if (conv.id != CONVERSATION.id)
                            continue;
                        CONVERSATION_MEMBERS = conv.members;
                        CONVERSATION_MEMBERS_NEW = CONVERSATION_MEMBERS.slice();
                    }

                    request('/conversation/load', args, 
                        (response) => 
                        {
                            $('#conv_'+CONVERSATION.id+'_link').removeClass('fw-bold');

                            for (var conv of CONVERSATION_LIST)
                            {
                                if (conv.id == CONVERSATION.id)
                                    $('#currentChannel').text(conv.name);
                            }
                            if (conversationChanged)
                                $('#chatBody').empty();
                            for (var message of response.list)
                            {
                                addMessage(message, args.action);
                                CONVERSATION_MINIMAL_MESSAGE_ID = Math.min(message.id, CONVERSATION_MINIMAL_MESSAGE_ID);
                            }
                            if (doRepaint)
                                ;
                            else if (!doPrepend || doAppend || doFirst)
                                $('.chat-body').scrollTop(99999);
                            else
                               $('.chat-body').scrollTop(1);
                        }, 
                        (error) =>
                        {
                            toast('fail', 'Ошибка загрузки диалога: ' + error.message);
                        });

                } break;
                case '/conversation/send':
                {
                    request('/conversation/send', args, 
                        (response) => 
                        {
                            toast('ok', 'Сообщение отправлено');
                        }, 
                        (error) =>
                        {
                            toast('fail', 'Ошибка отправки сообщения: ' + error.message);
                        });
                } break;
                case 'hashchanged':
                {
                    var hash = window.location.hash;
                    if (LAST_HASH === hash)
                        return;
                    LAST_HASH = hash;
                    var match = hash.match(/conv_(\d+)/);
                    if (!match)
                        return;
                    var number = match[1];

                    pushEvent('/conversation/load', {'id': number, 'offset_from': -1, 'offset_size': 20, 'action': 'first'});
                } break;
            }
        }

        function pushEvent(type, args)
        {
            processEvent(type, args);
        }

        $(document).ready(() => {
            $('#formContainer').fadeIn();
            $('#connectForm').fadeIn();

            $('#loginFormButton').click(() => {
                pushEvent('/uctl/login', {'login': $('#loginFormLogin').val(), 'password': $('#loginFormPassword').val()});
            });
            $('#loginFormBackToRegButton').click(() => {
                $('#formContainer').fadeIn();
                $('#loginForm').fadeOut();
                $('#registerForm').fadeIn();
            });
            $('#registerFormButton').click(() => {
                pushEvent('/uctl/register', {'login': $('#registerFormLogin').val(), 'password': $('#registerFormPassword').val()});
            });
            $('#registerFormBackToLoginButton').click(() => {
                $('#formContainer').fadeIn();
                $('#registerForm').fadeOut();
                $('#loginForm').fadeIn();
            });
            $('#logoutButton').click(() => {
                pushEvent('/uctl/logout');
            });

            $('#createConversationButton').click(() => {
                $('#formContainer').fadeIn();
                $('#createConservationForm').fadeIn();
            });
            $('#createConservationFormBackButton').click(() => {
                $('#formContainer').fadeOut();
                $('#createConservationForm').fadeOut();
            });
            $('#createConservationFormButton').click(() => {
                pushEvent('/conversation/create', {'name': $('#createConservationFormName').val()});
            });

            $('#sendMessageForm').on('submit', function(e) {
                e.preventDefault();

                let message = $('#messageInput').val();
                if (!message) 
                    return;

                pushEvent('/conversation/send', {'id': CONVERSATION.id, 'text': message});
                $('#messageInput').val('');
            });

            $('.chat-body').on('scroll', function() {
                if ($(this).scrollTop() !== 0) 
                    return;

                pushEvent('/conversation/load', {'id': CONVERSATION.id, 'offset_from': CONVERSATION_MINIMAL_MESSAGE_ID - 1, 'offset_size': 5, 'action': 'prepend'});
            });

            window.addEventListener('hashchange', () => pushEvent('hashchanged'));

            $('#changeConversationButton').click(() => {
				$('#users_table_content').html('');
				$('#formContainer').fadeIn();
				$('#changeConservationForm').fadeIn();

				  $("#search-input").autocomplete({
				    source: function(requestData, response) {
				        request('/uctl/search', {'term': requestData.term}, 
				            (responseData) => 
				            {
				                const remappedList = responseData.list.map(item => ({
				                  label: item.name,
				                  value: item.id
				                }));
				                response(remappedList);
				            }, 
				            (error) =>
				            {
				                toast('fail', 'Ошибка поиска пользователей.');
				            });
				    },
				    minLength: 2,
				    select: function(event, ui) {
				        if (CONVERSATION_MEMBERS_NEW.includes(ui.item.value))
				            return;
				        CONVERSATION_MEMBERS_NEW.push(ui.item.value);
				
				        $("#users-table tbody").append("<tr data-id='"+ui.item.value+"'><td>" + ui.item.label + "</td><td><button class='btn btn-danger remove-user-btn'>Удалить</button></td></tr>");
				    }
				  });

                for (var memberId of CONVERSATION_MEMBERS_NEW)
                {
                    $("#users-table tbody").append("<tr data-id='"+memberId+"'><td id='member_"+memberId+"'>???</td><td><button class='btn btn-danger remove-user-btn'>Удалить</button></td></tr>");
                    requestName(memberId, (name, memberId_)=>$('#member_'+memberId_).text(name));
                }

              // удаление пользователя
              $(document).on("click", ".remove-user-btn", function() {
                var row = $(this).parents("tr");
                var memberId = row.data("id");
                CONVERSATION_MEMBERS_NEW = CONVERSATION_MEMBERS_NEW.filter(x => x !== memberId);
                row.remove();
              });
            });
            $('#changeConservationFormBackButton').click(() => {
                $('#formContainer').fadeOut();
                $('#changeConservationForm').fadeOut();
            });
            $('#changeConservationFormButton').click(() => {
                const allMembers = [...new Set([...CONVERSATION_MEMBERS, ...CONVERSATION_MEMBERS_NEW])];

                for (const member of allMembers) 
                {
                    var removed = false;
                    var added = false;
                    if (CONVERSATION_MEMBERS.includes(member) && !CONVERSATION_MEMBERS_NEW.includes(member)) 
                    {
                        removed = true;
                    } else if (CONVERSATION_MEMBERS_NEW.includes(member) && !CONVERSATION_MEMBERS.includes(member)) 
                    {
                        added = true;
                    }
                    if (added || removed)
                            request('/conversation/add_remove_member', {'id': CONVERSATION.id, 'user_id': member, 'remove': removed}, 
                                (response) => 
                                {
                                    toast('ok', 'Пользователь #' + member + ' добавлен');
                                }, 
                                (error) =>
                                {
                                    toast('fail', 'Ошибка добавления пользователя #' + member + ': ' + error.message);
                                });
                }

                CONVERSATION_MEMBERS = CONVERSATION_MEMBERS_NEW.slice();
                CONVERSATION_MEMBERS_NEW = CONVERSATION_MEMBERS_NEW.slice();
            });
        });

        client.onConnect = () => pushEvent('connected');
        client.onStompError = function (frame) 
        {
       		console.log('Broker reported error: ' + frame.headers['message']);
        	console.log('Additional details: ' + frame.body);
        };

        client.activate();
