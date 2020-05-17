package ru.maltsev.reactiveredisexample.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
class RedisConfig {

    @Bean
    fun reactiveRedisTemplate(factory: ReactiveRedisConnectionFactory) : ReactiveRedisTemplate<String, String> {
        val serializationContext: RedisSerializationContext<String, String> = RedisSerializationContext
                .newSerializationContext<String, String>(StringRedisSerializer())
                .hashKey(StringRedisSerializer())
                .hashValue(Jackson2JsonRedisSerializer(String::class.java))
                .build()


        return ReactiveRedisTemplate(factory, serializationContext)
    }
}
