<?php

require_once "../Service/MetricsService.php";

class parallelRun
{
    function call()
    {
        $MetricsServ = new MetricsService();
        //Uncomment when deploying
        $MetricsServ->clearLog();
        $MetricsServ->clearResultFile();

        $mh = curl_multi_init();
        for ($i = 0; $i < 10; $i++) {
            $curls = Array();
            for ($j = 0; $j < 50; $j++) {
                $ch = curl_init();
                curl_setopt_array($ch, [
                    CURLOPT_URL => "http://localhost/Tema1CC/Controller/Api/getResults.php",
                    CURLOPT_RETURNTRANSFER => true,
                    CURLOPT_FOLLOWLOCATION => true,
                    CURLOPT_ENCODING => "",
                    CURLOPT_MAXREDIRS => 10,
                    CURLOPT_TIMEOUT => 30,
                    CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
                    CURLOPT_CUSTOMREQUEST => "GET"
                ]);
                array_push($curls, $ch);
                curl_multi_add_handle($mh, $ch);
            }
            do {
                $status = curl_multi_exec($mh, $active);
                if ($active) {
                    curl_multi_select($mh);
                }
            } while ($active && $status == CURLE_OK);
            for($j = 0; $j < 50; $j++){
                curl_multi_remove_handle($mh, $curls[$j]);
            }
        }
        curl_multi_close($mh);
    }
}

$parallelRunContr = new parallelRun();
$parallelRunContr->call();