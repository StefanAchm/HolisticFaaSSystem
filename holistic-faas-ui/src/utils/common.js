import HfApi from "@/utils/hf-api";

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

            ],

            providerOptions: [],

        };

    },

    methods: {

        init() {

            HfApi.getProviderOptions()
                .then(response => {
                    this.providerOptions = response.data;
                })

        },

        getRegions(provider) {

            return this.providerOptions
                .filter(po => po.provider === provider)
                .map(p => p.regions)
                .flat()
                .map(region => {
                    return {
                        title: region.regionCode + ': ' + region.regionName,
                        value: region.regionCode,
                        hint: region.regionName
                    }
                })

        },

        getRuntimes(provider) {

            return this.providerOptions
                .filter(po => po.provider === provider)
                .map(p => p.runtimes)
                .flat()
                .map(runtime => {
                    return {
                        title: runtime.runtimeCode,
                        value: runtime.runtimeCode,
                        hint: runtime.runtimeCode
                    }
                })

        }

    }

};