package jpabook.jpashop.domain

import javax.persistence.Embeddable

@Embeddable
class Address {
    var city: String? = null
    var street: String? = null
    var zipcode: String? = null

    protected constructor() {}
    constructor(city: String?, street: String?, zipcode: String?) {
        this.city = city
        this.street = street
        this.zipcode = zipcode
    }
}