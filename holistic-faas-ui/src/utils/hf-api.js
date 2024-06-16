import axios from 'axios';

const apiClient = axios.create({
    baseURL: window.env.VUE_APP_API_BASE_URL,
    withCredentials: true,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
    }
});

apiClient.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    }
)

export default {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Function

    addFunction(functionWithType) {
        return apiClient.post('/function/add', functionWithType);
    },

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Function type

    addFunctionType(functionType) {
        return apiClient.post('/function_type/add', functionType);
    },

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Function implementation

    updateFunction(file, data) {
        let formData = new FormData();

        formData.append('file', file);
        formData.append('apiFunction', new Blob([JSON.stringify(data)], {type: 'application/json'}));

        return apiClient.post('/function_implementation/update', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

    },

    uploadFunction(file, data) {

        let formData = new FormData();

        formData.append('file', file);
        formData.append('apiFunction', new Blob([JSON.stringify(data)], {type: 'application/json'}));

        return apiClient.post('/function_implementation/add', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

    },

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Function deployment

    addFunctionDeployment(data) {
        return apiClient.post('/function_deployment/add', data);
    },

    updateFunctionDeployment(data) {
        return apiClient.post('/function_deployment/update', data);
    },

    deployFunctionDeployment(id, awsSessionToken) {
        return apiClient.post(
            '/function_deployment/deploy/?',
            null,
            {params: {functionId: id, awsSessionToken: awsSessionToken}}
        );
    },


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // User and user credentials

    getUserCredentials(user) {
        // Need to set the user as body, not as query parameter
        return apiClient.get('/user_credentials/get', {
            params: {
                userId: user.id
            }
        });
    },

    uploadUserCredentials(file, data) {
        let formData = new FormData();

        formData.append('file', file);
        formData.append('apiUserCredentials', new Blob([JSON.stringify(data)], {type: 'application/json'}));

        return apiClient.post('/user_credentials/addOrUpdate', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
    },

    deleteUserCredentials(userId) {

        return apiClient.delete('/user_credentials/delete', {
            params: {
                userId: userId
            }
        });

    },

    register(user) {
        return apiClient.post('/user/register', user);
    },

    login(user) {
        return apiClient.post('/user/login', user);
    },


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Provider options and functions

    getProviderOptions() {
        return apiClient.get('/provider/getProviderOptions');
    },

    getAllFunctions() {
        return apiClient.get('/functionFlat/getAll');
    },

    prepareMigration(items, target, type) {

        let migrationRequest = {
            functions: items,
            target: target,
            migrationType: type
        }

        return apiClient.post('/function/prepareMigration', migrationRequest);

    },

    migrateFunctions(migrationRequest) {
        return apiClient.post('/function/migrate', migrationRequest);
    },

    downloadYaml(items) {
        return apiClient.post('/function/download', items, {
            responseType: 'blob'
        });
    },

    uploadAny(file, userId) {

        let formData = new FormData();

        formData.append('file', file);

        return apiClient.post('/function/upload', formData, {
            headers: {'Content-Type': 'multipart/form-data'},
            params: {userId: userId}
        });

    },

    uploadYaml(file, userId) {

        let formData = new FormData();

        formData.append('file', file);

        return apiClient.post('/function/uploadYaml', formData, {
            headers: {'Content-Type': 'multipart/form-data'},
            params: {userId: userId}
        });

    },

    uploadPackage(file, userId) {

        let formData = new FormData();

        formData.append('file', file);
        // formData.append('apiFunction', new Blob([JSON.stringify(data)], {type: 'application/json'}));

        return apiClient.post('/function/uploadPackage', formData, {
            headers: {'Content-Type': 'multipart/form-data'},
            params: {userId: userId}
        });

    },

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Workflow

    getAllWorkflows() {
        return apiClient.get('/workflow/getAll');
    },

    getWorkflow(id) {
        return apiClient.get('/workflow/get', {
            params: {
                id: id
            }
        });
    },
    getDeployments(id) {
        return apiClient.get('/workflow/' + id +'/getDeployments');
    },

    uploadWorkflow(file, workflow) {

        let formData = new FormData();

        formData.append('file', file);
        formData.append('workflow', new Blob([JSON.stringify(workflow)], {type: 'application/json'}));

        return apiClient.post('/workflow/add', formData, {
            headers: {'Content-Type': 'multipart/form-data'},
        });
    },

    getWorkflowFunctionImplementations(id) {
        return apiClient.get('/workflow/' + id + '/implementations');
    },


    prepareWorkflowDeployment(id) {
        return apiClient.post('/workflow/' + id + '/prepareWorkflowDeployment');
    },

    createWorkflow(workflow) {
        return apiClient.post('/workflow/add', workflow);
    },

    downloadWorkflow(workflowId) {
        return apiClient.post('/workflow/' + workflowId + '/download',
            {responseType: 'blob'});
    },

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Workflow deployment

    createWorkflowDeployment(editItems) {
        return apiClient.post('/workflow_deployment/add', editItems);
    },


    getWorkflowFunctionDeployments(id) {
        return apiClient.get('/workflow_deployment/' + id + '/getFunctionDeployments');
    },

    getWorkflowDeployment(id) {
        return apiClient.get('/workflow_deployment/get/',
            {params: {id: id}}
        );
    },

    migrateWorkflowDeployment(migration) {
        return apiClient.post('/workflow_deployment/migrate', migration);
    },

    downloadWorkflowDeployment(id) {
        return apiClient.post('/workflow_deployment/' + id + '/downloadYaml',
            {responseType: 'blob'});
    },

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Other


    getSystemInfo() {
        return apiClient.get('/system/info');
    },


}