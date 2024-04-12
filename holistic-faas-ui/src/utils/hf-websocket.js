
const ws = new WebSocket('ws://localhost:8080/hf/websocket');

export default {
    // connect() {
    //     return new Promise((resolve, reject) => {
    //         this.ws = new WebSocket('ws://localhost:8080/hf/websocket');
    //         this.ws.onopen = () => {
    //             resolve();
    //         };
    //         this.ws.onerror = (error) => {
    //             reject(error);
    //         };
    //     });
    // },
    send(data) {
        ws.send(JSON.stringify(data));
    },
    onMessage(callback) {
        ws.onmessage = (message) => {
            callback(JSON.parse(message.data));
        };
    },
    close() {
        ws.close();
    }
}