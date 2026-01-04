/**
 * VietTech ChatBot - Tư vấn mua hàng AI
 * Version 1.1 - With Markdown Support
 */

class VietTechChatBot {
    constructor() {
        this.isOpen = false;
        this.messages = [];
        this.isTyping = false;
        
        this.init();
    }
    
    init() {
        this.createChatbotHTML();
        this.bindEvents();
        this.addWelcomeMessage();
    }
    
    /**
     * Parse Markdown text to HTML
     * Supports: bold, italic, code, code blocks, lists, links, headers, blockquotes
     */
    parseMarkdown(text) {
        if (!text) return '';

        let html = text;

        // Escape HTML first to prevent XSS
        html = html.replace(/&/g, '&amp;')
                   .replace(/</g, '&lt;')
                   .replace(/>/g, '&gt;');

        // Code blocks (```code```) - must be processed first
        html = html.replace(/```(\w*)\n?([\s\S]*?)```/g, (match, lang, code) => {
            return `<pre class="md-code-block"><code class="language-${lang}">${code.trim()}</code></pre>`;
        });

        // Inline code (`code`)
        html = html.replace(/`([^`]+)`/g, '<code class="md-inline-code">$1</code>');

        // Headers (### Header)
        html = html.replace(/^### (.+)$/gm, '<h4 class="md-header">$1</h4>');
        html = html.replace(/^## (.+)$/gm, '<h3 class="md-header">$1</h3>');
        html = html.replace(/^# (.+)$/gm, '<h2 class="md-header">$1</h2>');

        // Bold (**text** or __text__)
        html = html.replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>');
        html = html.replace(/__([^_]+)__/g, '<strong>$1</strong>');

        // Italic (*text* or _text_)
        html = html.replace(/\*([^*]+)\*/g, '<em>$1</em>');
        html = html.replace(/_([^_]+)_/g, '<em>$1</em>');

        // Strikethrough (~~text~~)
        html = html.replace(/~~([^~]+)~~/g, '<del>$1</del>');

        // Links [text](url)
        html = html.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank" class="md-link">$1</a>');

        // Blockquotes (> text)
        html = html.replace(/^&gt; (.+)$/gm, '<blockquote class="md-blockquote">$1</blockquote>');

        // Unordered lists (- item or * item)
        html = html.replace(/^[\-\*] (.+)$/gm, '<li class="md-list-item">$1</li>');
        html = html.replace(/(<li class="md-list-item">.*<\/li>\n?)+/g, '<ul class="md-list">$&</ul>');

        // Ordered lists (1. item)
        html = html.replace(/^\d+\. (.+)$/gm, '<li class="md-list-item-ordered">$1</li>');
        html = html.replace(/(<li class="md-list-item-ordered">.*<\/li>\n?)+/g, '<ol class="md-list-ordered">$&</ol>');

        // Horizontal rule (---)
        html = html.replace(/^---$/gm, '<hr class="md-hr">');

        // Line breaks (convert remaining \n to <br>)
        html = html.replace(/\n/g, '<br>');

        // Clean up extra <br> after block elements
        html = html.replace(/<\/pre><br>/g, '</pre>');
        html = html.replace(/<\/ul><br>/g, '</ul>');
        html = html.replace(/<\/ol><br>/g, '</ol>');
        html = html.replace(/<\/blockquote><br>/g, '</blockquote>');
        html = html.replace(/<\/h[234]><br>/g, (match) => match.replace('<br>', ''));

        return html;
    }

    createChatbotHTML() {
        // Create toggle button
        const toggleBtn = document.createElement('button');
        toggleBtn.className = 'chatbot-toggle';
        toggleBtn.id = 'chatbotToggle';
        toggleBtn.innerHTML = `
            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M12 2C6.48 2 2 6.48 2 12C2 14.76 3.12 17.26 4.92 19.05L3.5 22L6.95 20.58C8.44 21.49 10.16 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2Z" fill="white"/>
                <circle cx="8" cy="12" r="1.5" fill="#4361ee"/>
                <circle cx="12" cy="12" r="1.5" fill="#4361ee"/>
                <circle cx="16" cy="12" r="1.5" fill="#4361ee"/>
            </svg>
            <span class="notification-badge" style="display: none;">1</span>
        `;
        
        // Create chatbot container
        const container = document.createElement('div');
        container.className = 'chatbot-container';
        container.id = 'chatbotContainer';
        container.innerHTML = `
            <!-- Header -->
            <div class="chatbot-header">
                <div class="chatbot-header-left">
                    <div class="chatbot-logo">
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 5C13.66 5 15 6.34 15 8C15 9.66 13.66 11 12 11C10.34 11 9 9.66 9 8C9 6.34 10.34 5 12 5ZM12 19.2C9.5 19.2 7.29 17.92 6 15.98C6.03 13.99 10 12.9 12 12.9C13.99 12.9 17.97 13.99 18 15.98C16.71 17.92 14.5 19.2 12 19.2Z" fill="white"/>
                        </svg>
                    </div>
                    <div class="chatbot-info">
                        <h4>VietTech ChatBot</h4>
                        <div class="chatbot-status">Online</div>
                    </div>
                </div>
                <button class="chatbot-close" id="chatbotClose">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <circle cx="12" cy="12" r="10" stroke="white" stroke-width="2"/>
                        <path d="M15 9L9 15M9 9L15 15" stroke="white" stroke-width="2" stroke-linecap="round"/>
                    </svg>
                </button>
            </div>
            
            <!-- Messages -->
            <div class="chatbot-messages" id="chatbotMessages">
                <!-- Messages will be added here -->
            </div>
            
            <!-- Footer -->
            <div class="chatbot-footer">
                <div class="chatbot-tags">
                    <button class="chatbot-tag" data-question="Sản phẩm nổi bật">🔥 Sản phẩm hot</button>
                    <button class="chatbot-tag" data-question="Hướng dẫn mua hàng">🛒 Cách mua hàng</button>
                    <button class="chatbot-tag" data-question="Chính sách bảo hành">🛡️ Bảo hành</button>
                </div>
                <div class="chatbot-input-area">
                    <input type="text" class="chatbot-input" id="chatbotInput" placeholder="Nhập tin nhắn của bạn...">
                    <button class="chatbot-send" id="chatbotSend">
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M22 2L11 13" stroke="#4361ee" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            <path d="M22 2L15 22L11 13L2 9L22 2Z" stroke="#4361ee" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                        </svg>
                    </button>
                </div>
            </div>
        `;
        
        document.body.appendChild(toggleBtn);
        document.body.appendChild(container);
        
        // Store references
        this.toggleBtn = toggleBtn;
        this.container = container;
        this.messagesContainer = document.getElementById('chatbotMessages');
        this.input = document.getElementById('chatbotInput');
    }
    
    bindEvents() {
        // Toggle chatbot
        this.toggleBtn.addEventListener('click', () => this.toggle());
        
        // Close chatbot
        document.getElementById('chatbotClose').addEventListener('click', () => this.close());
        
        // Send message
        document.getElementById('chatbotSend').addEventListener('click', () => this.sendMessage());
        
        // Enter key to send
        this.input.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.sendMessage();
            }
        });
        
        // Quick tags
        document.querySelectorAll('.chatbot-tag').forEach(tag => {
            tag.addEventListener('click', () => {
                const question = tag.dataset.question;
                this.input.value = question;
                this.sendMessage();
            });
        });
    }
    
    toggle() {
        this.isOpen = !this.isOpen;
        this.container.classList.toggle('active', this.isOpen);
        this.toggleBtn.classList.toggle('active', this.isOpen);
        
        // Hide notification badge when opened
        if (this.isOpen) {
            this.toggleBtn.querySelector('.notification-badge').style.display = 'none';
        }
        
        if (this.isOpen) {
            this.input.focus();
        }
    }
    
    open() {
        this.isOpen = true;
        this.container.classList.add('active');
        this.toggleBtn.classList.add('active');
        this.input.focus();
    }
    
    close() {
        this.isOpen = false;
        this.container.classList.remove('active');
        this.toggleBtn.classList.remove('active');
    }
    
    addWelcomeMessage() {
        const welcomeMessages = [
            'Xin chào! 👋 Tôi là VietTech ChatBot - trợ lý tư vấn mua hàng của bạn.',
            'Tôi có thể giúp bạn:\n• Tìm kiếm sản phẩm phù hợp\n• Tư vấn cấu hình điện thoại, laptop\n• Giải đáp thắc mắc về đơn hàng\n• Thông tin khuyến mãi\n\nBạn cần hỗ trợ gì hôm nay? 😊'
        ];
        
        welcomeMessages.forEach((msg, index) => {
            setTimeout(() => {
                this.addBotMessage(msg);
            }, index * 500);
        });
    }
    
    sendMessage() {
        const text = this.input.value.trim();
        if (!text || this.isTyping) return;
        
        // Add user message
        this.addUserMessage(text);
        this.input.value = '';
        
        // Show typing indicator
        this.showTyping();
        
        // Simulate bot response
        setTimeout(() => {
            this.hideTyping();
            this.getBotResponse(text);
        }, 1000 + Math.random() * 1000);
    }
    
    addUserMessage(text) {
        const time = this.getCurrentTime();
        const messageHTML = `
            <div class="chat-message user">
                <div class="message-avatar user-avatar">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M12 12C14.21 12 16 10.21 16 8C16 5.79 14.21 4 12 4C9.79 4 8 5.79 8 8C8 10.21 9.79 12 12 12ZM12 14C9.33 14 4 15.34 4 18V20H20V18C20 15.34 14.67 14 12 14Z" fill="#495057"/>
                    </svg>
                </div>
                <div>
                    <div class="message-content">${this.escapeHtml(text)}</div>
                    <div class="message-time">${time} ✓</div>
                </div>
            </div>
        `;
        this.messagesContainer.insertAdjacentHTML('beforeend', messageHTML);
        this.scrollToBottom();
    }
    
    addBotMessage(text) {
        const time = this.getCurrentTime();
        // Use Markdown parser for bot messages
        const formattedText = this.parseMarkdown(text);
        const messageHTML = `
            <div class="chat-message bot">
                <div class="message-avatar bot-avatar">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 5C13.66 5 15 6.34 15 8C15 9.66 13.66 11 12 11C10.34 11 9 9.66 9 8C9 6.34 10.34 5 12 5ZM12 19.2C9.5 19.2 7.29 17.92 6 15.98C6.03 13.99 10 12.9 12 12.9C13.99 12.9 17.97 13.99 18 15.98C16.71 17.92 14.5 19.2 12 19.2Z" fill="white"/>
                    </svg>
                </div>
                <div>
                    <div class="message-content markdown-content">${formattedText}</div>
                    <div class="message-time">${time}</div>
                    <div class="message-actions">
                        <button onclick="vietTechChatBot.copyMessage(this)" title="Sao chép">
                            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <rect x="9" y="9" width="13" height="13" rx="2" stroke="white" stroke-width="2"/>
                                <path d="M5 15H4C2.89543 15 2 14.1046 2 13V4C2 2.89543 2.89543 2 4 2H13C14.1046 2 15 2.89543 15 4V5" stroke="white" stroke-width="2"/>
                            </svg>
                        </button>
                        <button onclick="vietTechChatBot.likeMessage(this)" title="Hữu ích">
                            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M7 22H4C3.46957 22 2.96086 21.7893 2.58579 21.4142C2.21071 21.0391 2 20.5304 2 20V13C2 12.4696 2.21071 11.9609 2.58579 11.5858C2.96086 11.2107 3.46957 11 4 11H7M14 9V5C14 4.20435 13.6839 3.44129 13.1213 2.87868C12.5587 2.31607 11.7956 2 11 2L7 11V22H18.28C18.7623 22.0055 19.2304 21.8364 19.5979 21.524C19.9654 21.2116 20.2077 20.7769 20.28 20.3L21.66 11.3C21.7035 11.0134 21.6842 10.7207 21.6033 10.4423C21.5225 10.1638 21.3821 9.90629 21.1919 9.68751C21.0016 9.46873 20.7661 9.29393 20.5016 9.17522C20.2371 9.0565 19.9499 8.99672 19.66 9H14Z" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            </svg>
                        </button>
                        <button onclick="vietTechChatBot.dislikeMessage(this)" title="Không hữu ích">
                            <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                <path d="M17 2H19.67C20.236 1.98999 20.7859 2.18813 21.2154 2.55681C21.6449 2.92549 21.9242 3.43905 22 4V11C21.9242 11.561 21.6449 12.0745 21.2154 12.4432C20.7859 12.8119 20.236 13.01 19.67 13H17M10 15V19C10 19.7956 10.3161 20.5587 10.8787 21.1213C11.4413 21.6839 12.2044 22 13 22L17 13V2H5.72C5.23767 1.99454 4.76962 2.16359 4.40212 2.47599C4.03462 2.78839 3.79234 3.22309 3.72 3.7L2.34 12.7C2.29651 12.9866 2.31583 13.2793 2.39666 13.5577C2.4775 13.8362 2.61791 14.0937 2.80814 14.3125C2.99838 14.5313 3.23386 14.7061 3.49839 14.8248C3.76291 14.9435 4.0501 15.0033 4.34 15H10Z" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
                            </svg>
                        </button>
                    </div>
                </div>
            </div>
        `;
        this.messagesContainer.insertAdjacentHTML('beforeend', messageHTML);
        this.scrollToBottom();
    }
    
    showTyping() {
        this.isTyping = true;
        const typingHTML = `
            <div class="chat-message bot" id="typingIndicator">
                <div class="message-avatar bot-avatar">
                    <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                        <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 5C13.66 5 15 6.34 15 8C15 9.66 13.66 11 12 11C10.34 11 9 9.66 9 8C9 6.34 10.34 5 12 5ZM12 19.2C9.5 19.2 7.29 17.92 6 15.98C6.03 13.99 10 12.9 12 12.9C13.99 12.9 17.97 13.99 18 15.98C16.71 17.92 14.5 19.2 12 19.2Z" fill="white"/>
                    </svg>
                </div>
                <div class="typing-indicator">
                    <span></span>
                    <span></span>
                    <span></span>
                </div>
            </div>
        `;
        this.messagesContainer.insertAdjacentHTML('beforeend', typingHTML);
        this.scrollToBottom();
    }
    
    hideTyping() {
        this.isTyping = false;
        const typingIndicator = document.getElementById('typingIndicator');
        if (typingIndicator) {
            typingIndicator.remove();
        }
    }
    
    getBotResponse(userMessage) {
        // Call ChatServlet API to get AI response
        fetch(contextPath + '/api/chat', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ question: userMessage })
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Parse the response from ChatService
                let botResponse = data.response;

                // Try to extract answer from nested JSON if present
                try {
                    const parsedResponse = JSON.parse(botResponse);
                    // Support various response formats
                    if (parsedResponse.answer) {
                        botResponse = parsedResponse.answer;
                    } else if (parsedResponse.message) {
                        botResponse = parsedResponse.message;
                    } else if (parsedResponse.text) {
                        botResponse = parsedResponse.text;
                    } else if (parsedResponse.content) {
                        botResponse = parsedResponse.content;
                    } else if (parsedResponse.response) {
                        botResponse = parsedResponse.response;
                    }
                } catch (e) {
                    // If not JSON, use the response as is (already markdown text)
                    console.log('Response is plain text/markdown, using as is');
                }

                this.addBotMessage(botResponse);
            } else {
                this.addBotMessage('Xin lỗi, đã có lỗi xảy ra. Vui lòng thử lại sau! 😅');
            }
        })
        .catch(error => {
            console.error('Chat API error:', error);
            // Fallback to local response if API fails
            this.getLocalBotResponse(userMessage);
        });
    }

    getLocalBotResponse(userMessage) {
        const lowerMessage = userMessage.toLowerCase();
        let response = '';
        
        // Sản phẩm nổi bật / hot
        if (lowerMessage.includes('sản phẩm') && (lowerMessage.includes('hot') || lowerMessage.includes('nổi bật') || lowerMessage.includes('bán chạy'))) {
            response = '🔥 Sản phẩm hot tại VietTech:\n\n📱 Điện thoại:\n• iPhone 15 Pro Max - 34.990.000đ\n• Samsung Galaxy S24 Ultra - 31.990.000đ\n• Xiaomi 14 Ultra - 23.990.000đ\n\n💻 Laptop:\n• MacBook Pro M3 - 45.990.000đ\n• Dell XPS 15 - 42.990.000đ\n• ASUS ROG Strix - 35.990.000đ\n\nBạn quan tâm đến sản phẩm nào? 🛒';
        }
        // Hướng dẫn mua hàng
        else if (lowerMessage.includes('mua hàng') || lowerMessage.includes('cách mua') || lowerMessage.includes('đặt hàng')) {
            response = '🛒 Hướng dẫn mua hàng tại VietTech:\n\n1️⃣ Chọn sản phẩm yêu thích\n2️⃣ Nhấn "Thêm vào giỏ hàng"\n3️⃣ Vào giỏ hàng, kiểm tra đơn\n4️⃣ Nhập địa chỉ giao hàng\n5️⃣ Chọn phương thức thanh toán\n6️⃣ Xác nhận đặt hàng\n\n💡 Mẹo: Đăng ký thành viên để nhận ưu đãi độc quyền!\n\nBạn cần hỗ trợ gì thêm không? 😊';
        }
        // Bảo hành
        else if (lowerMessage.includes('bảo hành')) {
            response = '🛡️ Chính sách bảo hành VietTech:\n\n✅ Bảo hành chính hãng 12-24 tháng\n✅ 1 đổi 1 trong 30 ngày đầu\n✅ Hỗ trợ kỹ thuật miễn phí trọn đời\n✅ Trung tâm bảo hành toàn quốc\n\n📞 Hotline bảo hành: 1900-xxxx\n📧 Email: support@viettech.vn\n\nBạn có thắc mắc gì về bảo hành không?';
        }
        // Chào hỏi
        else if (lowerMessage.includes('xin chào') || lowerMessage.includes('hello') || lowerMessage.includes('hi') || lowerMessage.includes('chào')) {
            response = 'Xin chào bạn! 👋\n\nRất vui được hỗ trợ bạn hôm nay. Mình có thể giúp gì cho bạn?\n\n• Tư vấn sản phẩm\n• Thông tin khuyến mãi\n• Hỗ trợ đơn hàng\n• Chính sách bảo hành\n\nCứ hỏi mình nhé! 😊';
        }
        // Cảm ơn
        else if (lowerMessage.includes('cảm ơn') || lowerMessage.includes('thanks') || lowerMessage.includes('thank')) {
            response = 'Không có gì ạ! 😊\n\nRất vui vì đã hỗ trợ được bạn. Nếu cần thêm gì, cứ nhắn mình nhé!\n\nChúc bạn mua sắm vui vẻ tại VietTech! 🛒✨';
        }
        // Mặc định
        else {
            response = 'Cảm ơn bạn đã liên hệ! 😊\n\nMình có thể hỗ trợ bạn:\n• 🔥 Xem sản phẩm hot\n• 🛒 Hướng dẫn mua hàng\n• 💰 Thông tin khuyến mãi\n• 🛡️ Chính sách bảo hành\n• 🚚 Thông tin giao hàng\n\nBạn muốn tìm hiểu về vấn đề gì?';
        }
        
        this.addBotMessage(response);
    }
    
    getCurrentTime() {
        const now = new Date();
        return now.getHours().toString().padStart(2, '0') + ':' + 
               now.getMinutes().toString().padStart(2, '0');
    }
    
    scrollToBottom() {
        setTimeout(() => {
            this.messagesContainer.scrollTop = this.messagesContainer.scrollHeight;
        }, 100);
    }
    
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
    
    copyMessage(button) {
        const messageContent = button.closest('.chat-message').querySelector('.message-content').textContent;
        navigator.clipboard.writeText(messageContent).then(() => {
            // Show copied feedback
            const originalTitle = button.title;
            button.title = 'Đã sao chép!';
            setTimeout(() => {
                button.title = originalTitle;
            }, 2000);
        });
    }
    
    likeMessage(button) {
        button.style.background = '#28a745';
        setTimeout(() => {
            button.style.background = '#7b2cbf';
        }, 1000);
    }
    
    dislikeMessage(button) {
        button.style.background = '#dc3545';
        setTimeout(() => {
            button.style.background = '#7b2cbf';
        }, 1000);
    }
}

// Initialize chatbot when DOM is ready
let vietTechChatBot;
document.addEventListener('DOMContentLoaded', function() {
    vietTechChatBot = new VietTechChatBot();
});
