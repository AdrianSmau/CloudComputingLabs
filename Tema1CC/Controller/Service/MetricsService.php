<?php

class MetricsService
{
    private $logPath = "C:\\Users\\adria\\XAMPP\\htdocs\\Tema1CC\\Controller\\Service\\Logs\\LogFile.txt";
    private $resultPath = "C:\\Users\\adria\\XAMPP\\htdocs\\Tema1CC\\Controller\\Service\\Results\\WebSearchResults.txt";
    function writeToLog($apiName, $request, $response, $result, $latency)
    {
        $myfile = fopen($this->logPath, "a") or die("Unable to open log file!");
        $logEntry = "[" . date('m/d/Y h:i:s a', time()) . "] API = " . $apiName . ", REQUEST = " . $request . ", RESPONSE = " . $response . ", RESULT = " . $result .  ", LATENCY = " . $latency . " second(s).\n";
        fwrite($myfile, $logEntry);
        fclose($myfile);
    }
    function readLog()
    {
        echo "<h2>--- BEGINNING OF LOG FILE ---<h2><br>";
        $logFile = fopen($this->logPath, 'r');
        while ($line = fgets($logFile)) {
            echo "<h3>" . $line . "</h3>";
        }
        echo "<br>";
        echo ("<h2>--- END OF LOG FILE ---</h2>");
        fclose($logFile);
    }
    function readResultFile()
    {
        echo "<h2>--- BEGINNING OF RESULT FILE ---<h2><br>";
        $resultFile = fopen($this->resultPath, 'r');
        while ($line = fgets($resultFile)) {
            echo "<h3>" . $line . "</h3>";
        }
        echo "<br>";
        echo ("<h2>--- END OF RESULT FILE ---</h2>");
        fclose($resultFile);
    }
    function clearLog()
    {
        file_put_contents($this->logPath, "");
    }
    function clearResultFile()
    {
        file_put_contents($this->resultPath, "");
    }
}
