<?php

require_once "MetricsService.php";

class RandomOrgService
{
    function getRandomGeniusSongIndex()
    {
        $metricsServ = new MetricsService();

        $curl = curl_init();

        curl_setopt_array($curl, [
            CURLOPT_URL => "https://www.random.org/integers/?num=1&min=0&max=19&col=1&base=10&format=plain&rnd=new",
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_FOLLOWLOCATION => true,
            CURLOPT_ENCODING => "",
            CURLOPT_MAXREDIRS => 10,
            CURLOPT_TIMEOUT => 30,
            CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
            CURLOPT_CUSTOMREQUEST => "GET"
        ]);

        $start = microtime(true);
        $response = curl_exec($curl);
        $err = curl_error($curl);
        $timeElapsed = microtime(true) - $start;

        curl_close($curl);

        if ($err) {
            $metricsServ->writeToLog("RandomORG", "GET random number between 0 and 19", $err, null, $timeElapsed);
            return;
        } else {
            $metricsServ->writeToLog("RandomORG", "GET random number between 0 and 19", "200OK", $response, $timeElapsed);
            return $response;
        }
    }
}
