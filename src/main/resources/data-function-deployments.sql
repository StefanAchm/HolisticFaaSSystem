INSERT INTO hf.public.function_deployments (id, handler, memory, provider, region, runtime, timeout_secs, user_username,
                                            function_id)
VALUES ('aa4017a2-89e2-4bdc-9cc1-4d84562f8e1d', 'com.asi.hsg.HelloWorldHandler::handleRequest', 128, 'AWS', 'US_WEST_1',
        'JAVA_17', 3, 'stefan01', 'e0a22918-954d-4bf4-9527-04b28dd24173');

INSERT INTO hf.public.function_deployments (id, handler, memory, provider, region, runtime, timeout_secs, user_username,
                                            function_id)
VALUES ('3a850814-053a-4cb7-8310-3ae1b1d6af21', 'com.asi.hsg.HelloWorldHandler', 128, 'GCP', 'US_WEST1', 'JAVA_17', 3,
        'stefan01', 'f69d7424-0390-477c-93d8-6250881166fc');