<?php

require_once "MetricsService.php";

class WebSearchService
{
    private $keyPath = "C:\\Users\\adria\\XAMPP\\htdocs\\Tema1CC\\Controller\\Service\\Keys\\WebSearchKey.txt";
    private $resultFilePath = "C:\\Users\\adria\\XAMPP\\htdocs\\Tema1CC\\Controller\\Service\\Results\\WebSearchResults.txt";

    private function getKey()
    {
        $file = fopen($this->keyPath, 'r');
        $key = fgets($file);
        fclose($file);
        return $key;
    }

    function getWebSearches($toBeSearched)
    {
        $metricsServ = new MetricsService();

        if ($toBeSearched == null) {
            $metricsServ->writeToLog("WebSearch", "GET web searches by song title", "500 INTERNAL SERVER ERROR", null, 0);
            return;
        }

        $toBeSearched = str_replace(' ', '%20', $toBeSearched);

        $curl = curl_init();

        curl_setopt_array($curl, [
            CURLOPT_URL => "https://contextualwebsearch-websearch-v1.p.rapidapi.com/api/Search/WebSearchAPI?q=" . $toBeSearched . "&pageNumber=1&pageSize=10&autoCorrect=true",
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_ENCODING => "",
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 30,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => "GET",
            CURLOPT_HTTPHEADER => [
                "x-rapidapi-host: contextualwebsearch-websearch-v1.p.rapidapi.com",
                "x-rapidapi-key: " . $this->getKey()
            ],
        ]);

        $start = microtime(true);
        $response = curl_exec($curl);
        $err = curl_error($curl);
        $fetchTime = date('m/d/Y h:i:s a', time());
        $timeElapsed = microtime(true) - $start;

        curl_close($curl);

        $results = Array();

        if ($err) {
            $metricsServ->writeToLog("WebSearch", "GET web searches by song title", $err, null, $timeElapsed);
            return;
        } else {
            for ($i = 0; $i < 10; $i++) {
                $title = json_decode($response)->value[$i]->title;
                $url = json_decode($response)->value[$i]->url;
                $fullMessage =  "Web search result #" . $i . " - Title: " . $title . ", URL: " . $url . "!";
                $this->writeToResultFile($fetchTime, $fullMessage);
                array_push($results, $fullMessage);
            }
            $metricsServ->writeToLog("WebSearch", "GET web searches by song title", "200OK", "Result file updated! Titles displayed!", $timeElapsed);
            return $results;
        }
    }

    private function writeToResultFile($fetchingTime, $message)
    {
        $myfile = fopen($this->resultFilePath, "a") or die("Unable to open result file!");
        fwrite($myfile, "[" . $fetchingTime . "] " . $message . "\n");
        fclose($myfile);
    }
}
