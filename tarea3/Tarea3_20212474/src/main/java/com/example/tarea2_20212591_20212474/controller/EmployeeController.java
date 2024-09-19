package com.example.tarea2_20212591_20212474.controller;

import com.example.tarea2_20212591_20212474.entity.Department;
import com.example.tarea2_20212591_20212474.entity.Employee;
import com.example.tarea2_20212591_20212474.entity.Job;
import com.example.tarea2_20212591_20212474.repository.DepartmentRepository;
import com.example.tarea2_20212591_20212474.repository.EmployeeRepository;
import com.example.tarea2_20212591_20212474.repository.JobRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {
    final EmployeeRepository employeeRepository;
    final JobRepository jobRepository;

    final DepartmentRepository departmentRepository;
    public EmployeeController(EmployeeRepository employeeRepository, JobRepository jobRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.jobRepository = jobRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping({"employee/list", "employee"})
    public String listarEmpleados(@RequestParam(value = "search", required = false) String search, Model model) {
        List<Employee> listaEmpleados;
        if (search != null && !search.isEmpty()) {
            // Buscar empleados por nombre, apellido, puesto, departamento o ciudad
            listaEmpleados = employeeRepository.findBySearchCriteria(search.toLowerCase());
        } else {
            // Si no hay búsqueda, devolver todos los empleados
            listaEmpleados = employeeRepository.findAll();
        }
        model.addAttribute("employeeList", listaEmpleados);
        return "listaEmpleados";
    }
    @GetMapping({"employee/create"})
    public String crearEmpleado(Model model) {
        List<Job> jobs = jobRepository.findAll();
        List<Department> listaDepartamentos = departmentRepository.findAll();
        List<Employee> managers = employeeRepository.findManagers();
        model.addAttribute("employee", new Employee());
        model.addAttribute("managers",managers);
        model.addAttribute("jobs",jobs);
        model.addAttribute("departmentList", listaDepartamentos);
        return "crearEmpleado";
    }
    @GetMapping("employee/edit/{id}")
    public String informEmpleado(@PathVariable("id") Integer employeeId, Model model) {
        Optional<Employee> optEmployee = employeeRepository.findById(employeeId);
        if(optEmployee.isPresent()){
            Employee employee = optEmployee.get();
            model.addAttribute("employee",employee);
            model.addAttribute("selectedJobId", employee.getJob().getJobId());
            model.addAttribute("selectedDepartmentId", employee.getDepartment().getId());
            model.addAttribute("selectedManagerId", employee.getManager().getId());
        }
        List<Employee> managers = employeeRepository.findManagers();
        List<Job> jobs = jobRepository.findAll();
        List<Department> listaDepartamentos = departmentRepository.findAll();

        model.addAttribute("managers",managers);
        model.addAttribute("jobs",jobs);
        model.addAttribute("departmentList", listaDepartamentos);

        return "infoEmpleado";
    }
    @GetMapping("/delete")
    public String borrarEmpleado(Model model, @RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        Optional<Employee> optEmployee = employeeRepository.findById(id);

        if (optEmployee.isPresent()) {
            try {
                employeeRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("exito", "Se borró el empleado exitosamente.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "No se puede borrar el empleado.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Empleado no encontrado.");
        }
        return "redirect:/employee/list";
    }
    @PostMapping("/employee/save")
    public String guardarEmpleado(Employee employee, RedirectAttributes redirectAttributes) {

        if(employee.getHireDate() == null){
            employee.setHireDate(LocalDate.now());
        }
        try {

            employeeRepository.save(employee);
            redirectAttributes.addFlashAttribute("exito", "Empleado creado exitosamente.");
        } catch (Exception e) {
            System.out.println(e);
            redirectAttributes.addFlashAttribute("error", "No se puede crear el empleado.");
        }

        return "redirect:/employee/list";
    }
    @PostMapping("/employee/update")
    public String actualizarEmpleado(Employee employee,@RequestParam("id") int id,RedirectAttributes redirectAttributes) {
        Optional<Employee> empleadoExistente = employeeRepository.findById(id);

        if (empleadoExistente.isPresent()) {
            Employee empleadoActualizar = empleadoExistente.get();

            empleadoActualizar.setFirstName(employee.getFirstName());
            empleadoActualizar.setLastName(employee.getLastName());
            empleadoActualizar.setEmail(employee.getEmail());
            empleadoActualizar.setSalary(employee.getSalary());

            if (employee.getJob() != null) {
                empleadoActualizar.setJob(employee.getJob());
            }

            if (employee.getManager() != null) {
                empleadoActualizar.setManager(employee.getManager());
            }

            if (employee.getDepartment() != null) {
                empleadoActualizar.setDepartment(employee.getDepartment());
            }

            employeeRepository.save(empleadoActualizar);
            redirectAttributes.addFlashAttribute("exito", "Empleado actualizado exitosamente.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Empleado no encontrado.");
        }

        return "redirect:/employee/list";
    }


}
