package pl.uam.wmi.niezbednikstudenta;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import pl.uam.wmi.niezbednikstudenta.entities.*;
import pl.uam.wmi.niezbednikstudenta.services.*;

import java.io.FileReader;
import java.util.Iterator;

@Service
public class DataLoader {

    private JSONParser parser = new JSONParser();

    private final StudiesMajorService studiesMajorService;
    private final TermService termService;
    private final UserService userService;
    private final CourseTypeService courseTypeService;
    private final CoordinatorDegreeService coordinatorDegreeService;

    public DataLoader(StudiesMajorService studiesMajorService, TermService termService, UserService userService, CourseTypeService courseTypeService, CoordinatorDegreeService coordinatorDegreeService) {
        this.studiesMajorService = studiesMajorService;
        this.termService = termService;
        this.userService = userService;
        this.courseTypeService = courseTypeService;
        this.coordinatorDegreeService = coordinatorDegreeService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadDataToDb() {

        loadStudiesMajorToDb();
        loadTermsToDb();
        loadAdminsToDb();
        loadCourseTypeToDb();
        loadCoordinatorsDegree();
    }

    private void loadStudiesMajorToDb() {

        try {
            Object obj = parser.parse(new FileReader(ResourceUtils.getFile("classpath:StudiesMajor.json")));

            JSONArray studiesMajors = (JSONArray) obj;

            if (studiesMajors.size() != studiesMajorService.findAll().size()) {

                Iterator<JSONObject> iterator = studiesMajors.iterator();
                while (iterator.hasNext()) {

                    JSONObject studiesMajorJson = iterator.next();
                    String studiesMajorName = (String) studiesMajorJson.get("name");

                    if (!studiesMajorService.findByName(studiesMajorName).isPresent()) {

                        StudiesMajor studiesMajor = new StudiesMajor();
                        studiesMajor.setName(studiesMajorName);
                        studiesMajor.setNameEn((String) studiesMajorJson.get("nameEn"));
                        studiesMajorService.save(studiesMajor);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTermsToDb() {

        try {
            Object obj = parser.parse(new FileReader(ResourceUtils.getFile("classpath:Term.json")));

            JSONArray terms = (JSONArray) obj;

            if (terms.size() != termService.findAll().size()) {

                Iterator<JSONObject> iterator = terms.iterator();
                while (iterator.hasNext()) {
                    Term term = new Term();
                    JSONObject jsonTerm = iterator.next();
                    term.setName((String) jsonTerm.get("name"));
                    term.setNameEn((String) jsonTerm.get("nameEn"));
                    termService.save(term);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAdminsToDb() {
	// you can add here your account as a admin
    }

    private void loadCourseTypeToDb() {

        try {
            Object obj = parser.parse(new FileReader(ResourceUtils.getFile("classpath:CourseType.json")));

            JSONArray courseTypes = (JSONArray) obj;

            if (courseTypes.size() != courseTypeService.findAll().size()) {

                Iterator<JSONObject> iterator = courseTypes.iterator();
                while (iterator.hasNext()) {
                    JSONObject courseTypeJson = iterator.next();
                    CourseType courseType = new CourseType();
                    courseType.setName((String) courseTypeJson.get("name"));
                    courseType.setNameEn((String) courseTypeJson.get("nameEn"));
                    courseTypeService.save(courseType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCoordinatorsDegree() {

        try {
            Object obj = parser.parse(new FileReader(ResourceUtils.getFile("classpath:CoordinatorDegree.json")));

            JSONArray coordinatorsDegree = (JSONArray) obj;

            if (coordinatorsDegree.size() != coordinatorDegreeService.findAll().size()) {

                Iterator<JSONObject> iterator = coordinatorsDegree.iterator();
                while (iterator.hasNext()) {
                    CoordinatorDegree coordinatorDegree = new CoordinatorDegree();
                    coordinatorDegree.setName((String) iterator.next().get("name"));
                    coordinatorDegreeService.save(coordinatorDegree);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
