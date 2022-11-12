package jpabook.jpashop.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

@Entity
class Delivery (
    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    var order: Order? = null,

    @Embedded
    var address: Address? = null,

    @Enumerated(EnumType.STRING)
    val status : DeliveryStatus? = null,
)   {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    val id: Long? = null
}