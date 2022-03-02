<?php

$uri = parse_url($_SERVER['REQUEST_URI'], PHP_URL_PATH);
$uri = explode('/', $uri);

if (isset($uri[3])) {
    if ($uri[3] != "getResults" && $uri[3] != "parallelRun" && $uri[3] != "metrics") {
        header("HTTP/1.1 404 Not Found");
        exit();
    } else {
        require_once "Controller/Api/MainController.php";

        $objFeedController = new MainController();
        $strMethodName = $uri[3] . 'Action';
        $objFeedController->{$strMethodName}();
    }
} else {
    echo ('<head>
              <title> Cloud Computing Homework 1 </title>
              <link href="index.css" rel="stylesheet" type="text/css">
           </head>
           <body>
              <div class="resultsContainer">
                 <button class="resultsButton">getResults</button>
              </div>
              <div class="metricsContainer">
                 <button class="metricsButton">metrics</button>
              </div>
              <div class="parallelRunContainer">
                 <button class="parallelRunButton">parallelRun</button>
              </div>
              <script src="index.js"></script>
           </body>');
}
