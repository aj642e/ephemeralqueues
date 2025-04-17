package com.ephemeralqueue.api.controller;

import com.ephemeralqueue.engine.queuecollection.QueueCollection;
import com.ephemeralqueue.engine.queuecollection.entities.QueueId;
import com.ephemeralqueue.engine.queuecollection.entities.QueueValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QueueController {
  private final QueueCollection queueCollection;

  public QueueController() {
    this.queueCollection = new QueueCollection();
  }

  @PostMapping("/queue")
  public int create() {
    QueueId id = queueCollection.createQueue();
    return id.id();
  }

  @PostMapping("/queue/{id}/addition/{value}")
  public void add(@PathVariable int id,
                  @PathVariable int value) {
    queueCollection.add(id, value);
  }

  @GetMapping("/polling/{id}")
  public Integer poll(@PathVariable int id) {
    QueueValue val = queueCollection.poll(id);
    return val.value();
  }

  @DeleteMapping("/queue/{id}")
  public void delete(@PathVariable int id) {
    queueCollection.deleteQueue(id);
  }
}