<?php

require_once "../Service/MetricsService.php";

class metrics
{
    function call()
    {
        $MetricsServ = new MetricsService();

        $MetricsServ->readLog();
        $MetricsServ->readResultFile();
    }
}

$MetricsContr = new metrics();
$MetricsContr->call();
