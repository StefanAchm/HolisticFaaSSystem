
---------------------------------------
-- AWS

INSERT INTO hf.public.function_deployments (id, status, handler, memory, provider, region, runtime, timeout_secs, user_username,
                                            function_id)
VALUES ('aa4017a2-89e2-4bdc-9cc1-4d84562f8e1d', 'CREATED', 'com.asi.hsg.HelloWorldHandler::handleRequest', 128, 'AWS', 'US_WEST_1',
        'JAVA_17', 3, 'Stefan AWS', 'e0a22918-954d-4bf4-9527-04b28dd24173');

-- INSERT INTO hf.public.function_deployments (id, handler, memory, provider, region, runtime, timeout_secs, user_username,
--                                             function_id)
-- VALUES ('bd5f1f42-c6da-4f08-ad84-8c46f363f06d', 'com.asi.hsg.HelloWorldHandler::handleRequest', 128, 'AWS', 'US_WEST_1',
--         'JAVA_17', 3, 'stefan01', 'a2e59c8e-5683-4a3c-aea6-3e9c7adf5d51');

---------------------------------------
-- GCP

INSERT INTO hf.public.function_deployments (id, status, handler, memory, provider, region, runtime, timeout_secs, user_username,
                                            function_id)
VALUES ('3a850814-053a-4cb7-8310-3ae1b1d6af21', 'CREATED', 'com.asi.hsg.HelloWorldHandler', 128, 'GCP', 'US_WEST1', 'JAVA_17', 3,
        'Stefan GCP', 'f69d7424-0390-477c-93d8-6250881166fc');


-- INSERT INTO hf.public.function_deployments (id, handler, memory, provider, region, runtime, timeout_secs, user_username,
--                                             function_id)
-- VALUES ('c25b621e-50d6-49ed-9e85-bf74bd625e71', 'com.asi.hsg.HelloWorldHandler', 128, 'GCP', 'US_WEST1', 'JAVA_17', 3,
--         'stefan01', '0c2fbc2f-f50f-404e-8dd3-c9f23bca7ab4');