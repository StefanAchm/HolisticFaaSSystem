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



    getAllUsers() {
        return apiClient.get('/user/getAll');
    },

    getAllFunctions() {
        return apiClient.get('/function/getAll');
    },

    getProviderOptions() {
        return apiClient.get('/provider/getProviderOptions');
    },

    deployFunction(data) {
        return apiClient.post('/deploy/add', data);
    },

    deployFunctionDeploy(id) {
        // Properties.API_IP + "/deploy/deploy", null,
        //           {
        //             headers: {'Content-Type': 'application/json'},
        //             params: {functionId: item.id, localOnly: false} // TODO: set localOnly to false
        //           })
        return apiClient.get('/deploy/deploy/' + id);
    },

    uploadFunction(file, data) {

        let formData = new FormData();

        formData.append('file', file);
        formData.append('apiFunction', new Blob([JSON.stringify(data)], {type: 'application/json'}));

        return apiClient.post('/function/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });

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

    getAllDeployments() {
        return apiClient.get('/deploy/getAll');
    },


}