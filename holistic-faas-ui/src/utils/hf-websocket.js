
let ws;

function connect() {
    ws = new WebSocket(window.env.VUE_APP_WS_BASE_URL);

    // Reconnect on close,
    ws.onclose = function() {
        setTimeout(function() {
            connect();
        }, 1000); // Try to reconnect in 1 second
    };

    ws.onerror = function() {
        ws.close();
    };
}

connect();

// Send heartbeat every 30 seconds
setInterval(() => {
    if (ws.readyState === ws.OPEN) {
        ws.send('heartbeat');
    }
}, 30000);

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