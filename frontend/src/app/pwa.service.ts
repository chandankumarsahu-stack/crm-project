import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PwaService {

  deferredPrompt: any;

  constructor() {
    window.addEventListener('beforeinstallprompt', (event: any) => {
      event.preventDefault();
      this.deferredPrompt = event;
    });
  }

  async installPwa() {
    if (!this.deferredPrompt) return;

    this.deferredPrompt.prompt();

    const result = await this.deferredPrompt.userChoice;

    console.log(result);

    this.deferredPrompt = null;
  }

  canInstall() {
    return this.deferredPrompt != null;
  }
}