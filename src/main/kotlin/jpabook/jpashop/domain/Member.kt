package jpabook.jpashop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Member (
    val name: String? = null,

    @Embedded
    val address: Address? = null,

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    val orders: MutableList<Order> = ArrayList()
) {
        @Id
        @GeneratedValue
        @Column(name = "member_id")
        val id: Long? = null
}