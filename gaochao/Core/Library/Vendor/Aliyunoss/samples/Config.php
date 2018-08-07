<?php

/**
 * Class Config
 *
 * 执行Sample示例所需要的配置，用户在这里配置好Endpoint，AccessId， AccessKey和Sample示例操作的
 * bucket后，便可以直接运行RunAll.php, 运行所有的samples
 */
final class Config
{
	const OSS_ACCESS_ID 	= 'LTAIuee6NSIn9qEY';
    const OSS_ACCESS_KEY 	= 'DrggdwBw4yPDU1egHxEQ6z9JaADcs3';
    const OSS_ENDPOINT 		= 'oss-cn-shanghai-internal.aliyuncs.com';//'oss-cn-shanghai.aliyuncs.com';  //nk-files.oss-cn-shanghai-internal.aliyuncs.com
    const OSS_TEST_BUCKET 	= 'nk-files';
}
