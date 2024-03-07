export default {
    data() {
        return {

            providers: [
                {
                    value: 'AWS',
                    title: 'Amazon Web Services',
                },
                {
                    value: 'GCP',
                    title: 'Google Cloud Platform',
                },

            ]

        };
    },
    methods: {
        // Shared methods
        openDialog() {
            this.dialog = true;
        },
        closeDialog() {
            this.dialog = false;
        },
        // Any other shared methods
    },
    // You can also include computed properties, watchers, lifecycle hooks, etc.
};