-- 若锁不存在：则新增锁，并设置锁重入计数为1、设置锁过期时间
if (redis.call('EXISTS', KEYS[1]) == 0) then
    redis.call('HSET', KEYS[1], 'count', 1);
    redis.call('HSET', KEYS[1], 'id', ARGV[1]);
    redis.call('EXPIRE', KEYS[1], ARGV[2]);
    return 1;
end;
-- 若锁存在，且唯一标识也匹配：则表明当前加锁请求为锁重入请求，故锁重入计数+1，并再次设置锁过期时间
if (redis.call('HGET', KEYS[1], 'id') == ARGV[1]) then
    local counter = redis.call('HINCRBY', KEYS[1], 'count', 1);
    redis.call('PEXPIREAT', KEYS[1], ARGV[2]);
    return counter;
end;
-- 若锁存在，但唯一标识不匹配：表明锁是被其他线程占用，当前线程无权解他人的锁
return -1;