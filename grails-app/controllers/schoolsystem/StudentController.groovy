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
    def detailStudent(){
        Long id= params.long("id")
        Student student=Student.get(id)
        render student as JSON


    }
    def updateStudent(){
        Long id=params.long("id")
        def requestJSON=request.JSON
        Student student=Student.get(id)
        student.firstName = requestJSON.firstName
        student.lastName = requestJSON.lastName
        student.age = requestJSON.age
        student.gender = requestJSON.gender
        student.email = requestJSON.email
        student.contactNumber = requestJSON.contactNumber
        student.dateOfBirth = utiDateService.convertStringToDate(requestJSON.dateOfBirth)
        student.save(flush:true)
        render student as JSON

    }
    def listStudent(){
        def student=Student.list()
        render student as JSON
    }
    def deleteStudent(){
        Long id=params.long("id")
        Student student=Student.get(id)
        student.delete(flush: true)
        render student as JSON






    }

}
