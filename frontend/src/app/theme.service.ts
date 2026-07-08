import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  constructor() {

    const saved=localStorage.getItem('theme')||'dark';

    this.setTheme(saved);

  }

  setTheme(theme:string){

    document.body.classList.remove('dark','light');

    document.body.classList.add(theme);

    localStorage.setItem('theme',theme);

  }

  toggleTheme(){

    const dark=document.body.classList.contains('dark');

    this.setTheme(dark?'light':'dark');

  }

}