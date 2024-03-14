//       editedItem: {
//         provider: '',
//         memory: '',
//         timeoutSecs: '',
//         handler: '',
//         region: '',
//         runtime: '',
//         userName: '',
//         functionId: ''
//       },

class Function {
    constructor(provider, memory, timeoutSecs, handler, region, runtime, userName, functionId) {
        this.provider = provider;
        this.memory = memory;
        this.timeoutSecs = timeoutSecs;
        this.handler = handler;
        this.region = region;
        this.runtime = runtime;
        this.userName = userName;
        this.functionId = functionId;
    }

}