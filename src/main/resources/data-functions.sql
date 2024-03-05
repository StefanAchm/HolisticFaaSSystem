CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- uuid_generate_v4()

INSERT INTO hf.public.functions (id, file_path, handler, memory, name, provider, region, runtime, timeout_secs,
                                 user_username)
VALUES (uuid_generate_v4(), 'zips/aws-helloworld-java-1.0-SNAPSHOT.zip', 'com.asi.hsg.HelloWorldHandler::handleRequest',
        128, 'function1', 'AWS', 'US_WEST_1', 'JAVA_17', 3, 'stefan01');

--         // {
--         //  "filePath": "zips/gcp-helloworld-java-1.0-SNAPSHOT.zip",
--         //  "handler": "com.asi.hsg.HelloWorldHandler",
--         //  "memory": 128,
--         //  "name": "HelloWorld-FromAPI-01",
--         //  "provider": "GCP",
--         //  "region": "US_WEST1",
--         //  "runtime": "JAVA_17",
--         //  "timeoutSecs": 3,
--         //  "user": "auth/stefan01/gcp.json"
--         //}

INSERT INTO hf.public.functions (id, file_path, handler, memory, name, provider, region, runtime, timeout_secs,
                                 user_username)
VALUES (uuid_generate_v4(), 'zips/gcp-helloworld-java-1.0-SNAPSHOT.zip', 'com.asi.hsg.HelloWorldHandler',
        128, 'HelloWorld-FromAPI-01', 'GCP', 'US_WEST1', 'JAVA_17', 3, 'stefan01');