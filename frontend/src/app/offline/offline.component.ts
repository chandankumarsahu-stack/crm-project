import { Component } from '@angular/core';

@Component({
  selector: 'app-offline',
  standalone: true,
  template: `
<div class="offline-container">

    <div class="offline-card">

        <div class="emoji">
            📡
        </div>

        <h2>No Internet Connection</h2>

        <p>
            ZING CRM is offline.<br>
            Please reconnect to continue.
        </p>

        <button class="btn btn-primary mt-3" (click)="reload()">
            Try Again
        </button>

    </div>

</div>
`,
styles:[`
.offline-container{
height:100vh;
display:flex;
justify-content:center;
align-items:center;
background:#081327;
color:white;
text-align:center;
padding:20px;
}

.offline-card{
max-width:350px;
}

.emoji{
font-size:70px;
margin-bottom:20px;
}
`]
})
export class OfflineComponent{

reload(){
location.reload();
}

}