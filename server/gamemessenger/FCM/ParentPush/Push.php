<?php 

namespace ideath;


class Push {
 
    // push message title
    private $title;
    private $message;
    private $type;
    private $notifSub;

   // private $image;
    // flag indicating whether to show the push
    // notification or not
    // this flag will be useful when perform some opertation
    // in background when push is recevied
 
    public function __construct() {    
    }
 
    public function setType($type) {
        $this->type = $type;
    }

    public function getType() {
        return $this->type;
    }

    public function setTitle($title) {
        $this->title = $title;
    }
 
    public function setMessage($message) {
        $this->message = $message;
    }

    public function setNotifSub($notifSub){
        $this->notifSub = $notifSub;
    }
    public function getPush() {
        $res = array();
        $res['data']['type'] = $this->type;
        $res['data']['title'] = $this->title;
        $res['data']['message'] = $this->message;
        $res['data']['date'] =  date('Y-m-d G:i:s');
        $res['data']['notifSub'] =  $this->notifSub;
        //$res['data']['image'] = $this->image;
       // $res['data']['payload'] = $this->data;
        return $res;
    }


     /*  public function setImage($imageUrl) {
        $this->image = $imageUrl;
    }*/
 
}