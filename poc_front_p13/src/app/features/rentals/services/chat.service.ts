import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private chatOpenSubject = new BehaviorSubject<boolean>(false);
  chatOpen$ = this.chatOpenSubject.asObservable();
  
  constructor() { }

  openChat() {
    this.chatOpenSubject.next(true);
  }

  closeChat() {
    this.chatOpenSubject.next(false);
  }
}
