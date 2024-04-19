export class FunctionDeployment {
    constructor({handler, runtime, memory, timeoutSecs, region, functionId, functionName, status, provider, userName} = {}) {

        this.handler = handler;
        this.runtime = runtime;
        this.memory = memory;
        this.timeoutSecs = timeoutSecs;
        this.region = region;
        this.functionId = functionId;
        this.functionName = functionName;
        this.status = status;
        this.provider = provider;
        this.userName = userName;

    }
}