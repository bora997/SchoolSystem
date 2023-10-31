package schoolsystem

import grails.converters.JSON

class StudentController {

    UtiDateService utiDateService

    def createStudent(){
        Student createStu = new Student()
        def requestJSON = request.JSON
        createStu.firstName = requestJSON.firstName
        createStu.lastName = requestJSON.lastName
        createStu.age = requestJSON.age
        createStu.gender = requestJSON.gender
        createStu.email = requestJSON.email
        createStu.contactNumber = requestJSON.contactNumber
        createStu.dateOfBirth = utiDateService.convertStringToDate(requestJSON.dateOfBirth)
        createStu.save(flush:true)
        render createStu as JSON


    }
}
