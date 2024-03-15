
export class CloudFunction {

    constructor({status, provider, memory, timeoutSecs, handler, region, runtime, userName, functionId, functionName} = {}) {

        this.status = status;
        this.provider = provider;
        this.memory = memory;
        this.timeoutSecs = timeoutSecs;
        this.handler = handler;
        this.region = region;
        this.runtime = runtime;
        this.userName = userName;
        this.functionId = functionId;
        this.functionName = functionName;

    }

}