package jpabook.jpashop.service

import jpabook.jpashop.domain.Member
import jpabook.jpashop.repository.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class MemberService {
    private val memberRepository: MemberRepository? = null

    /**
     * 회원 가입
     */
    @Transactional
    fun join(member: Member): Long? {
        validateDuplicateMember(member) //중복 회원 검증
        memberRepository!!.save(member)
        return member.id
    }

    private fun validateDuplicateMember(member: Member) {
        val findMembers = memberRepository!!.findByName(member.name)
        check(findMembers.isEmpty()) { "이미 존재하는 회원입니다." }
    }

    //회원 전체 조회
    fun findMembers(): List<Member> {
        return memberRepository!!.findAll()
    }

    fun findOne(memberId: Long?): Member? {
        return memberRepository!!.findOne(memberId!!)
    }
}