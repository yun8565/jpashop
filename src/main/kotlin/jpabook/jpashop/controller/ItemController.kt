package jpabook.jpashop.controller

import jpabook.jpashop.domain.item.Book
import jpabook.jpashop.service.ItemService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Controller
class ItemController {
    private val itemService: ItemService? = null
    @GetMapping("/items/new")
    fun createForm(model: Model): String {
        model.addAttribute("form", BookForm())
        return "items/createItemForm"
    }

    @PostMapping("/items/new")
    fun create(form: BookForm): String {
        val book = Book(form.author, form.isbn)
        book.name = form.name
        book.price = form.price!!
        book.stockQuantity = form.stockQuantity!!

        itemService!!.saveItem(book)
        return "redirect:/"
    }

    @GetMapping("/items")
    fun list(model: Model): String {
        val items = itemService!!.findItems()
        model.addAttribute("items", items)
        return "items/itemList"
    }

    @GetMapping("items/{itemId}/edit")
    fun updateItemForm(@PathVariable("itemId") itemId: Long?, model: Model): String {
        val item : Book = itemService!!.findOne(itemId) as Book
        val form = BookForm(
            id = item?.id,
            name = item?.name,
            price = item?.price,
            stockQuantity = item.stockQuantity,
            author = item.author,
            isbn = item.isbn
        )

        model.addAttribute("form", form)
        return "items/updateItemForm"
    }

    @PostMapping("items/{itemId}/edit")
    fun updateItem(@PathVariable itemId: Long?, @ModelAttribute("form") form: BookForm): String {
        itemService!!.updateItem(itemId, form.name, form.price!!, form.stockQuantity!!)
        return "redirect:/items"
    }
}