
const ws = new WebSocket('ws://localhost:8080/hf/websocket');

export default {

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