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

    uploadUserCredentials(file, data) {
        let formData = new FormData();

        formData.append('file', file);
        formData.append('apiUser', new Blob([JSON.stringify(data)], {type: 'application/json'}));

        return apiClient.post('/user/create', formData, {
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

    prepareMigration(items, target) {

        let migrationRequest = {
            functions: items,
            target: target,
            migrationType: 'FUNCTION_PROVIDER' // TODO: dynamic
        }

        return apiClient.post('/function/prepareMigration', migrationRequest);

    },

    // deprecated
    migrateFunctions(migrationRequest) {
        return apiClient.post('/function/migrate', migrationRequest);
    }

}