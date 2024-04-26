import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/hf/api',
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

    deployFunctionDeployment(id) {
        return apiClient.post(
            '/function_deployment/deploy/?', // + new URLSearchParams({functionId: id, localOnly: false}));
            null,
            {params: {functionId: id, localOnly: false}}
        );
    },


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // User and user credentials

    getAllUsers() {
        return apiClient.get('/user_credentials/getAll');
    },

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
        return apiClient.get('/function/getAll');
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
    // Provider options and functions

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

    uploadWorkflow(file) {

        let formData = new FormData();

        formData.append('file', file);

        return apiClient.post('/workflow/add', formData, {
            headers: {'Content-Type': 'multipart/form-data'},
            // params: {userId: userId}
        });
    },

    // @GetMapping(value = "/{workflowId}/implementations")
    getWorkflowFunctionImplementations(id) {
        return apiClient.get('/workflow/' + id + '/implementations');
    },
    createWorkflowDeployment(editItems) {
        return apiClient.post('/workflow_deployment/add', editItems);
    }
}