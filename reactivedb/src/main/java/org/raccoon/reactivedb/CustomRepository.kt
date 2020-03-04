package org.raccoon.reactivedb

import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.findById
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.remove
import org.springframework.data.mongodb.core.find
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import sun.jvm.hotspot.tools.jcore.NameFilter
import javax.annotation.PostConstruct

@Repository
class CustomRepository(private val template: ReactiveMongoTemplate){
    companion object {
    val initialCustomers = listOf(Customer(1,"Kotlin"),
            Customer(2,"Spring"),Customer(3,"Microservice"),
            Customer(3,"aa", Customer.Telephone("44", "123")))
    }

    @PostConstruct
    fun initializeRepository() = initialCustomers.map(Customer::toMono).map(this::create).map(Mono<Customer>::subscribe)

    fun create(customer: Mono<Customer>) = template.save(customer)
    fun findById(id:Int)=template.findById<Customer>(id)
    fun deleteById(id:Int)=template.remove<Customer>(Query(where("_id").isEqualTo(id)))
    fun findCustomer(nameFilter: NameFilter)=template.find<Customer>(
            Query(where("name").regex(".*$nameFilter.*","i"))
    )
}
//모노나 플럭스같은 리액티브 타입으로 작업을 수행하기 위해서 바꿈


/*
interface CustomRepository : ReactiveCrudRepository<Customer, Int>{
    fun create(customer: Mono<Customer>) = template.save(customer)
}
 */