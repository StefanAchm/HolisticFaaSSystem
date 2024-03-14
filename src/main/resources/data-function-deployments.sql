
---------------------------------------
-- AWS

INSERT INTO hf.public.function_deployments (id, status, handler, memory, provider, region, runtime, timeout_secs, user_username,
                                            function_id)
VALUES ('aa4017a2-89e2-4bdc-9cc1-4d84562f8e1d', 'CREATED', 'com.asi.hsg.HelloWorldHandler::handleRequest', 128, 'AWS', 'EU_WEST_1',
        'JAVA_17', 3, 'Stefan AWS', 'e0a22918-954d-4bf4-9527-04b28dd24173');

INSERT INTO hf.public.function_deployments (id, status, handler, memory, provider, region, runtime, timeout_secs, user_username,
                                            function_id)
VALUES ('abf3fbf3-efeb-4927-bbce-999dfd32466e', 'CREATED', 'com.asi.hsg.HelloWorldHandler::handleRequest', 128, 'AWS', 'EU_WEST_2',
        'JAVA_17', 3, 'Stefan AWS', 'e0a22918-954d-4bf4-9527-04b28dd24173');

INSERT INTO hf.public.function_deployments (id, status, handler, memory, provider, region, runtime, timeout_secs, user_username,
                                            function_id)
VALUES ('0074ac7b-b6cd-4ca1-a100-d98b1cd715b6', 'CREATED', 'com.asi.hsg.HelloWorldHandler::handleRequest', 128, 'AWS', 'US_EAST_1',
        'JAVA_17', 3, 'Stefan AWS', 'e0a22918-954d-4bf4-9527-04b28dd24173');

INSERT INTO hf.public.function_deployments (id, status, handler, memory, provider, region, runtime, timeout_secs, user_username,
                                            function_id)
VALUES ('898ecf4e-5774-4a9a-b61f-c912e93eee2f', 'CREATED', 'com.asi.hsg.HelloWorldHandler::handleRequest', 128, 'AWS', 'US_EAST_2',
        'JAVA_17', 3, 'Stefan AWS', 'e0a22918-954d-4bf4-9527-04b28dd24173');

INSERT INTO hf.public.function_deployments (id, status, handler, memory, provider, region, runtime, timeout_secs, user_username,
                                            function_id)
VALUES ('92487f99-0d47-44df-ac07-0b642e3957f3', 'CREATED', 'com.asi.hsg.HelloWorldHandler::handleRequest', 128, 'AWS', 'US_WEST_1',
        'JAVA_17', 3, 'Stefan AWS', 'e0a22918-954d-4bf4-9527-04b28dd24173');

---------------------------------------
-- GCP

INSERT INTO hf.public.function_deployments (id, status, handler, memory, provider, region, runtime, timeout_secs, user_username,
                                            function_id)
VALUES ('3a850814-053a-4cb7-8310-3ae1b1d6af21', 'CREATED', 'com.asi.hsg.HelloWorldHandler', 128, 'GCP', 'US_WEST1', 'JAVA_17', 3,
        'Stefan GCP', 'f69d7424-0390-477c-93d8-6250881166fc');
