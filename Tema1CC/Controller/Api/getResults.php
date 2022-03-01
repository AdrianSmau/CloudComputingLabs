<?php

require_once "../Service/RandomOrgService.php";
require_once "../Service/GeniusService.php";
require_once "../Service/WebSearchService.php";
require_once "../Service/MetricsService.php";

class getResults
{
    function call()
    {
        $MetricsServ = new MetricsService();
        //Uncomment when deploying
        $MetricsServ->clearLog();
        $MetricsServ->clearResultFile();

        $start = microtime(true);
        $RandomOrgServ = new RandomOrgService();
        $randomSongID = $RandomOrgServ->getRandomGeniusSongIndex();
        $GeniusServ = new GeniusService();
        $selectedSongTitle = $GeniusServ->getArtistSongs($randomSongID);
        $WebSearchServ = new WebSearchService();
        $results = json_encode($WebSearchServ->getWebSearches($selectedSongTitle));
        $timeElapsed = microtime(true) - $start;

        if ($results == null) {
            $MetricsServ->writeToLog("getResults", "GET random number, generate random song by artist with id 16775 and fetch web searches", "500 INTERNAL SERVER ERROR", null, $timeElapsed);
            return;
        } else {
            $MetricsServ->writeToLog("getResults", "GET random number, generate random song by artist with id 16775 and fetch web searches", "200OK", "Result file updated! Call /metrics for info!", $timeElapsed);
            print_r($results);
            return $results;
        }
    }
}

$GetResultsContr = new getResults();
$GetResultsContr->call();
