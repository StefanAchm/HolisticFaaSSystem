
const ws = new WebSocket(window.env.VUE_APP_WS_BASE_URL);

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