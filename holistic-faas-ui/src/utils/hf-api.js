import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/hf/api',
    withCredentials: false,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
    }
});

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
        // Properties.API_IP + "/deploy/deploy", null,
        //           {
        //             headers: {'Content-Type': 'application/json'},
        //             params: {functionId: item.id, localOnly: false} // TODO: set localOnly to false
        //           })
        return apiClient.get('/function_deployment/deploy/' + id);
    },


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // User

    getAllUsers() {
        return apiClient.get('/user/getAll');
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

}