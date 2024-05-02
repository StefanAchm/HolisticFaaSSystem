
// const downloadFileService = {};

export default {

    downloadFile(data, fileName) {
        const blob = new Blob([data], {type: 'application/octet-stream'});
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
    }

    //  // Create a new Blob object using the response data of the file
    //             let blob = new Blob([response.data], {type: 'application/yaml'});
    //
    //             // Create a link element
    //             let link = document.createElement('a');
    //
    //             // Create an object URL for the file
    //             link.href = window.URL.createObjectURL(blob);
    //
    //             // Set the file name and download attribute
    //             link.download = this.workflow.name + '.yaml';
    //
    //             // Add the link to the DOM
    //             document.body.appendChild(link);
    //
    //             // Simulate clicking the link
    //             link.click();
    //
    //             // Remove the link from the DOM
    //             document.body.removeChild(link);
    //           }

}