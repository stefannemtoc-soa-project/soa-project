

# Angular + Spring + Nginx tutorial

Hi there! I will show you how to configure Nginx to handle a simple angular front and java API


## Angular frontend

We will start by creating the angular app:

`ng new my-awesome-app`

This will create a simple angular website and install the needed dependencies. 

After this, we can create an api service to handle the REST API

```ng generate service api```

This should create a simple service in `src/app/api.service.ts`. Open this file and let's start coding!

In the exported class, add a variable to remember the baseurl for the APIs

```
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';
  ...
```

Then, inject the httpClient in the constructor:

```
...
constructor(private httpClient: HttpClient) { }
...
```

Then, add a method that will execute the api and return it's response

```
getSomeData(): Observable<any> {
    return this.httpClient.get(`${this.apiUrl}/myData`);
}
```

Your file should look like this:
```
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private httpClient: HttpClient) { }

  getSomeData(): Observable<any> {
    return this.httpClient.get(`${this.apiUrl}/myData`);
  }

}
```

Check the imports carefully! It's easy to make mistakes!

Let's make the UI part. 

In `src/app/app.component.html` add a button and a text to show the REST response:

```
<div>
  <h1>Hello there!</h1>
  <button (click)="doAPi()">Click me!</button>
  <h2>Api response</h2>
  <p> {{responseData | json}}</p>
</div>
```

In `src/app/app.component.ts`, add a function that will be triggered when the button is clicked and save the response in a variable:

```
  responseData : any;

  constructor(private apiService: ApiService) {}

  doAPi() {
    this.apiService.getSomeData().subscribe(data => {
      this.responseData = data;
    })
  }
```

Don't forget to inject the ApiService. You might also need to provide the http client, you can do this by adding `provideHttpClient()` in the `providers` list from `app.config.ts`.

You can now run the application using `ng serve` and open it in a browser at `localhost:4200`.


## Java Backend

You can do this step directly in your IDE (IntelliJ for example). Go to [start.spring.io](https://start.spring.io/) and fill the information you need. You can choose your project manager, the language you want to use, the spring version and other stuff. The most important thing in this page is to add the spring web dependency. 

After this, download the project (or create it) and we can start coding.

Start by creating a simple rest controller:

```
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyController {

    @GetMapping("/hello")
    public String getHello() {
        return "Hello from this api!";
    }
}
```

This will make an API that responds to GET at "/api/hello".

You might also need to add a configuration to stop any CORS errors:

```
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("*");
    }
}

```


## Nginx

Start by installing nginx.

On windows, you need to go to [this](https://nginx.org/en/download.html) page and download the mainline version for windows. For linux, it is simply enough to run:

```
sudo apt install nginx
sudo systemctl start nginx
```

Let's configure it. After you installed nginx, you will find the configuration files in the `conf` folder. On linux, you will find it `/etc/nginx`.

Open `nginx.conf`. You will see a lot of commented code and a simple server.

Remove the existing server from `http` and add the following code for the backend part:

```
location /api {
            proxy_pass http://localhost:8080;

            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_set_header X-Forwarded-Proto $scheme;
        }
```

This will take all the requests that come through nginx and have /api in their path, and pass them through to the server. It will also add some headers to not confuse the server in this routing.


Let's add the location for frontend:

```
	location / {
	    proxy_pass http://localhost:4200;
        }
```

This will get all the requests we get through nginx (exception being the others already mentioned in the other location) and pass them through the frontend part of the application. 

You are almost done! Start the frontend and backend, and then run nginx. On windows, open a cmd in the nginx folder and run `./nginx.exe`. On linux, nginx is already running and you have to tell it to refresh it's configuration, using the command: 

`sudo systemctl reload nginx`.


You can now see that you can open the browser at [http://localhost/](http://localhost/) or [http://localhost:80/](http://localhost:80/).