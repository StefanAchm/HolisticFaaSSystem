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

    getAllFunctionTypes() {
        return apiClient.get('/function_type/getAll');
    },

    addFunctionType(functionType) {
        return apiClient.post('/function_type/add', functionType);
    },

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Function implementation


    getAllFunctionImplementations() {
        return apiClient.get('/function_implementation/getAll');
    },

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

    deployFunction(data) {
        return apiClient.post('/function_deployment/add', data);
    },

    deployFunctionDeploy(id) {
        return apiClient.post('/function_deployment/deploy/?' + new URLSearchParams({functionId: id, localOnly: false}));
    },


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // User

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
    // Other

    getProviderOptions() {
        return apiClient.get('/provider/getProviderOptions');
    },

    getAllFunctions() {
        return apiClient.get('/function/getAll');
    },

    // TODO: still needed?
    getAllDeployments() {
        return apiClient.get('/deploy/getAll');
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

    uploadYaml(file, userId) {

        let formData = new FormData();

        formData.append('file', file);
        // formData.append('apiFunction', new Blob([JSON.stringify(data)], {type: 'application/json'}));

        return apiClient.post('/function/uploadYaml?userId=' + userId, formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

    },

    uploadPackage(file) {

        // TODO: add user id!

        let formData = new FormData();

        formData.append('file', file);
        // formData.append('apiFunction', new Blob([JSON.stringify(data)], {type: 'application/json'}));

        return apiClient.post('/function/uploadPackage', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

    },

}