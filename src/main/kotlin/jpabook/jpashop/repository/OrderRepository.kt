package jpabook.jpashop.repository

import jpabook.jpashop.domain.Order
import org.springframework.stereotype.Repository
import org.springframework.util.StringUtils
import javax.persistence.EntityManager
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Repository
class OrderRepository(private val em: EntityManager) {
    fun save(order: Order?) {
        em.persist(order)
    }

    fun findOne(id: Long?): Order {
        return em.find(Order::class.java, id)
    }

    fun findAllByString(orderSearch: OrderSearch): List<Order> {
        var jpql = "select o from Order o join o.member m"
        var isFirstCondition = true

        //주문 상태 검색
        if (orderSearch.orderStatus != null) {
            if (isFirstCondition) {
                jpql += " where"
                isFirstCondition = false
            } else {
                jpql += " and"
            }
            jpql += " o.status = :status"
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.memberName)) {
            if (isFirstCondition) {
                jpql += " where"
                isFirstCondition = false
            } else {
                jpql += " and"
            }
            jpql += " m.name like :name"
        }
        var query = em.createQuery(
            jpql,
            Order::class.java
        )
            .setMaxResults(1000)
        if (orderSearch.orderStatus != null) {
            query = query.setParameter("status", orderSearch.orderStatus)
        }
        if (StringUtils.hasText(orderSearch.memberName)) {
            query = query.setParameter("name", orderSearch.memberName)
        }
        return query.resultList
    }

    /**
     * JPA Criteria
     */
    fun findAllByCriteria(orderSearch: OrderSearch): List<Order> {
        val cb = em.criteriaBuilder
        val cq = cb.createQuery(
            Order::class.java
        )
        val o = cq.from(
            Order::class.java
        )
        val m = o.join<Any, Any>("member", JoinType.INNER)
        val criteria: MutableList<Predicate> = ArrayList()

        //주문 상태 검색
        if (orderSearch.orderStatus != null) {
            //val status = cb.equal(o["status"], orderSearch.orderStatus)
            //criteria.add(status)
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.memberName)) {
            val name = cb.like(m.get("name"), "%" + orderSearch.memberName + "%")
            criteria.add(name)
        }
        cq.where(cb.and(*criteria.toTypedArray()))
        val query = em.createQuery(cq).setMaxResults(1000)
        return query.resultList
    }
}