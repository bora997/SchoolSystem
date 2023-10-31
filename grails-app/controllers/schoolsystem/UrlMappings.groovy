package schoolsystem

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
        //student
        "/student/create-student"(controller: "Student",action: "createStudent")
        "/student/listStudent"(controller: "Student",action: "listStudent")
    }
}
