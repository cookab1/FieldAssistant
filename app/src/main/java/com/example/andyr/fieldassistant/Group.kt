package com.andy.fieldassistant

/**
 * Created by andyr on 3/6/2018.
 */
class Group() {

    private lateinit var members: ArrayList<String>

    constructor(string: String) : this() {
        members = ArrayList<String>(1)
        members.add(string)
    }

    fun addMember(member: String) {
        members.add(member)
    }

    fun getMember(index : Int) : String? {
        var member : String? = null
        if(index < members.size) {
            member = members.get(index)
        }
        return member;
    }

    fun setMemebers(newMembers: ArrayList<String>) {
        members = newMembers
    }

    fun getMembers() : ArrayList<String>? {
        return members
    }

    override fun toString() : String {
        var string: String = ""

        //iterate through the arrayList compiling the email addresses as one String

        return string
    }
}