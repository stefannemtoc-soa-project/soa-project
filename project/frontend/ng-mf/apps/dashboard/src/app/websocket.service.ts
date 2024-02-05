import { Injectable } from "@angular/core";
import { Subject } from "rxjs";

declare var SockJS: any;
declare var Stomp: any;

@Injectable({
    providedIn: 'root',
})
export class WebsocketService {
    private socket : WebSocket | undefined;
    messageReceived: Subject<string> = new Subject<string>();

    public stompClient: any;
    
    constructor() {}

    connect() {

        const serverUrl = 'http://localhost:80/notification'
        const ws = new SockJS(serverUrl);
        this.stompClient = Stomp.over(ws);
        const that = this;
        this.stompClient.connect({}, function() {
            console.log('Websocket connection established');
            that.stompClient.subscribe('/notification', (message: any) => {
                if (message.body) {
                    that.messageReceived.next(message.body);
                }
            })
        })
        // this.socket = new WebSocket('ws://localhost:8080/notification')
        // this.socket.onopen = () => {
        //     console.log('Websocket connection established');
        // }
        // this.socket.onmessage = (event) => {
        //     const message = event.data;
        //     console.log('Received message: ', message);
        //     this.messageReceived.next(message);
        // }
        // this.socket.onclose = (event) => {
        //     console.log('Websocket connection closed: ', event);
        // }
        // this.socket.onerror = (error) => {
        //     console.log('Websocket error: ', error);
        // }
    }

    closeConnection() {
        this.socket?.close;
    }
}