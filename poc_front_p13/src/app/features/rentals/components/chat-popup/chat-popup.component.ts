import { Component, OnInit } from '@angular/core';
import { ChatService } from '../../services/chat.service';

@Component({
  selector: 'app-chat-popup',
  templateUrl: './chat-popup.component.html',
  styleUrls: ['./chat-popup.component.scss']
})
export class ChatPopupComponent implements OnInit {

  messages: { user: string; text: string }[] = [];
  messageText: string = '';
  isOpen = false;

  constructor(private chatService: ChatService) {
    this.chatService.chatOpen$.subscribe((state) => {
      this.isOpen = state;
    });
  }

  ngOnInit(): void {
  }

  sendMessage() {
    if (this.messageText.trim()) {
      this.messages.push({ user: 'Client', text: this.messageText });
      this.messageText = '';

      setTimeout(() => {
        this.messages.push({ user: 'Support', text: 'Merci pour votre message, nous vous répondrons sous peu.' });
      }, 2000);
    }
  }
  closeChat() {
    this.chatService.closeChat();
  }
}
