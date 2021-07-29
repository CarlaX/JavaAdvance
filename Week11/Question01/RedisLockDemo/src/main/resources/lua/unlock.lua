-- if redis.call("get",KEYS[1]) == ARGV[1]
-- then
--     return redis.call("del",KEYS[1]) > 0
-- else
--     return false
-- end
--

-- 若锁不存在：则直接广播解锁消息，并返回1
if (redis.call('EXISTS', KEYS[1]) == 0) then
--     redis.call('UNSUBSCRIBE', KEYS[1], ARGV[1]);
--     redis.call('PUBLISH', KEYS[1], ARGV[1]);
    return 0;
end;

-- 若锁存在，但唯一标识不匹配：则表明锁被其他线程占用，当前线程不允许解锁其他线程持有的锁
if (redis.call('HGET', KEYS[1], 'id') ~= ARGV[1]) then
    return -1;
end;

-- 若锁存在，且唯一标识匹配：则先将锁重入计数减1
local counter = redis.call('HINCRBY', KEYS[1], 'count', -1);

if (counter == 0) then
-- 锁重入计数减1后还大于0：表明当前线程持有的锁还有重入，不能进行锁删除操作，但可以友好地帮忙设置下过期时期
--     redis.call('pexpire', KEYS[1], ARGV[2]);
--     return 0;
-- else
-- 锁重入计数已为0：间接表明锁已释放了。直接删除掉锁，并广播解锁消息，去唤醒那些争抢过锁但还处于阻塞中的线程
    redis.call('DEL', KEYS[1]);
--     redis.call('UNSUBSCRIBE', KEYS[1], ARGV[1]);
--     redis.call('PUBLISH', KEYS[1], ARGV[1]);
    return 0;
end;

return 1;